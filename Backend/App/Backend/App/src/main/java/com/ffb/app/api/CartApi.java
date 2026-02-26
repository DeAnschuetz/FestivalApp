package com.ffb.app.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.ffb.app.service.api.api.cart.CartService;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.response.cart.CartItemResponse;
import com.ffb.model.api.response.cart.CartResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jspecify.annotations.NonNull;

@ApplicationScoped
@Path("cart")
public class CartApi {

	@Inject
	JsonWebToken jwt;
	private final CartService cartService;

	@Inject
	public CartApi(CartService cartService) {
		this.cartService = cartService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed({"GUEST", "ADMIN"})
	@Operation(summary = "Get the current Cart")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Cart",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponse.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getCart() throws ApiException {
		String loginNr = jwt.getName();
        CartResponse data;
        try {
            data = cartService.getCartByLoginNr(loginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();

	}

	@PUT
	@Path("add_cart_item")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Add a Item to the Cart Cart")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Cart",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponse.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addItemToCart( CartItemCreationRequest request ) throws ApiException {
		String loginNr = jwt.getName();
		UUID productId = request.productId();
		if(productId == null) {
			throw new ApiException("Product id must be provided.", Response.Status.BAD_REQUEST);
		}
		int itemCount = request.itemCount();
		if (itemCount <= 0) {
			throw new ApiException("Item count must be greater than 0.", Response.Status.BAD_REQUEST);
		}
		String extra = request.extra();
		if (extra != null && extra.length() > 255) {
			throw new ApiException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
		}

        CartResponse data;
        try {
            data = cartService.addItemToCart(loginNr, productId, itemCount, extra);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Update the Amount or Extra of a Cart Item")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Cart",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponse.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response updateCartItem(CartItemUpdateRequest request ) throws ApiException {
		String loginNr = jwt.getName();

		UUID cartItemId = request.cartItemId();
		if(cartItemId == null) {
			throw new ApiException("Cart item id must be provided.", Response.Status.BAD_REQUEST);
		}
		int itemCount = request.itemCount();
		if (itemCount <= 0) {
			throw  new ApiException("Item count must be greater than 0.", Response.Status.BAD_REQUEST);
		}
		String extra = request.extra();
		if (extra != null && extra.length() > 255) {
			throw new ApiException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
		}

		CartResponse data;
        try {
            data = cartService.updateCartItemById(loginNr, cartItemId, itemCount, extra);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Remove a Item from the Cart")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Cart",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponse.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response removeItemFromCart(@PathParam(value = "id")  UUID id) throws ApiException {
		String loginNr = jwt.getName();
		if(loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided", Response.Status.BAD_REQUEST);
		}
		if(id == null) {
			throw new ApiException("Cart item id must be provided", Response.Status.BAD_REQUEST);
		}
		CartResponse data;
		try {
			data = cartService.removeItemFromCart(loginNr, id);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.ok().entity(data).build();
	}

	@PUT
	@Path("{newPrio}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Change the Priority of the Cart")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Cart",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponse.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response changePrio(@PathParam(value = "newPrio") boolean newPrio) throws ApiException {
		String loginNr = jwt.getName();

		CartResponse data;
        try {
            data = cartService.changePrio(loginNr, newPrio);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@GET
	@Path("list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Get all the Carts")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "All the Carts",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponse.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listALl() {
		List<CartResponse> data = cartService.listAll();
		return Response.ok().entity(data).build();
	}

	/*
		Private Helper Functions
	*/


	private CartResponse getCartResponse(Cart cart) {
		List<CartItemResponse> items = new ArrayList<>();

		if (cart.getCartItems() != null) {
			for (CartItem i : cart.getCartItems()) {
				items.add(getCartItemResponse(i));
			}
		}

		return new CartResponse(cart.isHasPrio(), cart.getTotal(), items);
	}

	private CartItemResponse getCartItemResponse(CartItem cartItem) {
		Product product = cartItem.getProduct();
		List<CartItemResponse> subItems = product.getSubProducts().stream().map(subProduct -> getCartItemResponse(subProduct, cartItem.getItemCount())).toList();
		return new CartItemResponse(
				cartItem.getId(),
				product.getDisplayName(),
				product.getSymbolIdentifier(),
				cartItem.getPrice(),
				cartItem.getItemCount(),
				cartItem.getExtra(),
				subItems
		);
	}

	private CartItemResponse getCartItemResponse(Product product, int count) {
		return new CartItemResponse(
				product.getId(),
				product.getDisplayName(),
				product.getSymbolIdentifier(),
				0,
				count,
				null,
				null
		);
	}

}
