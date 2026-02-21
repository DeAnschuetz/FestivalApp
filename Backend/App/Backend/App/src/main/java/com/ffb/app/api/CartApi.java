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

@ApplicationScoped
@Path("/cart")
public class CartApi {

	private final CartService cartService;

	@Inject
	public CartApi(CartService cartService) {
		this.cartService = cartService;
	}

	@GET
	@Path("/by_login_nr/{loginNr}")
	@RolesAllowed("GUEST")
	public Response getCart(@PathParam(value = "loginNr") String loginNr) throws ApiException {
		if (loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided.");
		}
        CartSimple data = null;
        try {
            data = cartService.getCartByLoginNr(loginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("add/by_login_nr/{loginNr}")
	@RolesAllowed("GUEST")
	public Response addItemToCart(@PathParam(value = "loginNr") String loginNr, CartItemCreationRequest request ) throws ApiException {
		if (loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided.");
		}
		UUID productId = request.productId();
		if(productId == null) {
			throw new ApiException("Product id must be provided");
		}
		int itemCount = request.itemCount();
		if (itemCount <= 0) {
			throw new ApiException("Item count must be greater than 0");
		}
		String extra = request.extra();
		if (extra != null && extra.length() > 255) {
			throw new ApiException("Extra must be less than 255 characters");
		}

        CartSimple data = null;
        try {
            data = cartService.addItemToCart(loginNr, productId, itemCount, extra);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("update/by_login_nr/{loginNr}")
	@RolesAllowed("GUEST")
	public Response updateCartItem(@PathParam(value = "loginNr") String loginNr, CartItemUpdateRequest request ) throws ApiException {
		if (loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided");
		}
		UUID cartItemId = request.cartItemId();
		if(cartItemId == null) {
			throw new ApiException("Cart item id must be provided");
		}
		int itemCount = request.itemCount();
		if (itemCount <= 0) {
			throw  new ApiException("Item count must be greater than 0");
		}
		String extra = request.extra();
		if (extra != null && extra.length() > 255) {
			throw new ApiException("Extra must be less than 255 characters");
		}

        CartSimple data = null;
        try {
            data = cartService.updateCartItemById(loginNr, cartItemId, itemCount, extra);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@DELETE
	@Path("/by_login_nr/{loginNr}/{id}")
	@RolesAllowed("GUEST")
	public Response removeItemFromCart(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "id")  UUID id) throws ApiException {
		if(loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided");
		}
		if(id == null) {
			throw new ApiException("Cart item id must be provided");
		}
        CartSimple data = null;
        try {
            data = cartService.removeItemFromCart(loginNr, id);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

	@PUT
	@Path("/by_login_nr/{loginNr}/{newPrio}")
	@RolesAllowed("GUEST")
	public Response changePrio(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "newPrio") boolean newPrio) throws ApiException {
		if(loginNr == null || loginNr.isEmpty()) {
			throw new ApiException("LoginNr must be provided");
		}
        CartSimple data = null;
        try {
            data = cartService.changePrio(loginNr, newPrio);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.ok().entity(data).build();
	}

}
