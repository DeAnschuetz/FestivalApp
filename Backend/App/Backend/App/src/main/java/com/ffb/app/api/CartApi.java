package com.ffb.app.api;

import java.util.UUID;

import com.ffb.app.service.api.api.cart.CartService;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.response.cart.CartSimple;

import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

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
	@RolesAllowed({"GUEST", "ADMIN"})
	public Response getCart() throws ApiException {
		String loginNr = jwt.getName();
        CartSimple data;
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

        CartSimple data;
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

        CartSimple data;
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
	public Response removeItemFromCart(@PathParam(value = "id")  UUID id) throws ApiException {
		String loginNr = jwt.getName();
		if(loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided", Response.Status.BAD_REQUEST);
		}
		if(id == null) {
			throw new ApiException("Cart item id must be provided", Response.Status.BAD_REQUEST);
		}
		CartSimple data;
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
	public Response changePrio(@PathParam(value = "newPrio") boolean newPrio) throws ApiException {
		String loginNr = jwt.getName();

        CartSimple data;
        try {
            data = cartService.changePrio(loginNr, newPrio);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

}
