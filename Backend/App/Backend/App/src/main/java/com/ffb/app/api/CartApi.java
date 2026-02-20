package com.ffb.app.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ffb.model.api.request.cart.CartItemRequest;
import com.ffb.model.api.response.cart.CartItemSimple;
import com.ffb.model.api.response.cart.CartSimple;
import com.ffb.model.db.objects.cart.Cart;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/cart")
public class CartApi {

	@GET
	@Path("/by_login_nr/{loginNr}")
	public Response getCart(@PathParam(value = "loginNr") String loginNr) {

		List<CartItemSimple> cartItems = new ArrayList<>();
		CartSimple data = new CartSimple(false, 23, cartItems);

		Response response =  Response.ok().entity(data).build();
		return response;

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addItemToCart(CartItemRequest request ) {

		List<CartItemSimple> cartItems = new ArrayList<>();
		CartSimple data = new CartSimple(false, 23, cartItems);

		Response response =  Response.ok().entity(data).build();
		return response;
	}

	@DELETE
	@Path("/by_login_nr/{loginNr}/{id}")
	public Response removeItemFromCart(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "id")  UUID id) {

		List<CartItemSimple> cartItems_01 = new ArrayList<>();
		Response response = Response.ok().entity(cartItems_01).build();
		return response;
	}

	@PUT
	@Path("/by_login_nr/{loginNr}/{newPrio}")
	public Response changePrio(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "newPrio") boolean newPrio) {

		List<CartItemSimple> cartItems = new ArrayList<>();
		CartSimple data = new CartSimple(newPrio, 23, cartItems);

		Response response =  Response.ok().entity(data).build();
		return response;
	}

}
