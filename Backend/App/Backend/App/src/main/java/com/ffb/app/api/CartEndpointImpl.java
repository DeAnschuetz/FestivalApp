package com.ffb.app.api;

import java.util.List;
import java.util.UUID;
import com.ffb.app.service.api.cart.CartService;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.response.cart.CartResponseFull;
import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.api.response.error.ErrorResponse;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Path("cart")
public class CartEndpointImpl {

	// TODO Logging
	private final Logger LOG = LoggerFactory.getLogger(CartEndpointImpl.class);

	@Inject
	JsonWebToken jwt;
	private final CartService cartService;

	@Inject
	public CartEndpointImpl(CartService cartService) {
		this.cartService = cartService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed({"GUEST"})
	@Operation(summary = "Get the Cart for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
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
        CartResponseSimple data;
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
	@Operation(summary = "Add a Item to the Cart of the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
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
		validateItemCreationRequest(request);

		CartResponseSimple data;
        try {
            data = cartService.addItemToCart(loginNr, request);
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
	@Operation(summary = "Update the Amount or Extra of a Cart Item for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
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

		validateUpdateRequest(request);

		CartResponseSimple data;
        try {
            data = cartService.updateCartItemById(loginNr, request);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Remove a Item from the Cart for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
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
		CartResponseSimple data;
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
	@Operation(summary = "Change the Priority of the Cart for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
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

		CartResponseSimple data;
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
							schema = @Schema(implementation = CartResponseFull.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listALl() {
		List<CartResponseFull> data = cartService.listAll();
		return Response.ok().entity(data).build();
	}



	/*
		Private Helper Functions
	*/

	private static void validateUpdateRequest(CartItemUpdateRequest request) throws ApiException {
		if (request.cartItemId() == null) {
			throw new ApiException("Product id must be provided.", Response.Status.BAD_REQUEST);
		}
		if (request.itemCount() <= 0) {
			throw new ApiException("Item count must be greater than 0.", Response.Status.BAD_REQUEST);
		}
		if (request.extra() != null && request.extra().length() > 255) {
			throw new ApiException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
		}
	}

	private static void validateItemCreationRequest(CartItemCreationRequest request) throws ApiException {
		if (request.productId() == null) {
			throw new ApiException("Product id must be provided.", Response.Status.BAD_REQUEST);
		}
		if (request.itemCount() <= 0) {
			throw new ApiException("Item count must be greater than 0.", Response.Status.BAD_REQUEST);
		}
		if (request.extra() != null && request.extra().length() > 255) {
			throw new ApiException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
		}
	}
}
