package com.ffb.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ffb.model.request.cart.CartItemRequest;
import com.ffb.model.response.Response;
import com.ffb.model.response.cart.CartItemSimple;
import com.ffb.model.response.cart.CartSimple;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/cart")
public class CartApi {
	
	@PUT
	@Path("{loginNr}")
	public Response orderCart(String loginNr) {
		
		Response response = new Response(200, null, null);
		return response;
	}
	
	@GET
	@Path("{loginNr}")
	public Response getCart(@PathParam(value = "loginNr") String loginNr) {
		
		List<CartItemSimple> cartItems_01 = new ArrayList<>();
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Burger", (byte)0, 11.5, 1, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Pommes", (byte)0, 3.5, 2, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 3, null, null));
		
		List<CartItemSimple> cartItems_02 = new ArrayList<>();
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Pizza Magerita", (byte)0, 11.5, 1, null, null));
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 1, null, null));

		List<CartItemSimple> cartItems = new ArrayList<>();
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Burger Menü", (byte)0, 11.5, 1, null, cartItems_01));
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Pizza Menü", (byte)0, 11.5, 1, null, cartItems_02));

		
		CartSimple data = new CartSimple(false, 23, cartItems);
		
		Response response = new Response(200, null, data);
		return response;
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addItemToCart(CartItemRequest request ) {
		
		List<CartItemSimple> cartItems_01 = new ArrayList<>();
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Burger", (byte)0, 11.5, 1, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Pommes", (byte)0, 3.5, 2, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 3, null, null));
		
		List<CartItemSimple> cartItems_02 = new ArrayList<>();
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Pizza Magerita", (byte)0, 11.5, 1, null, null));
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 1, null, null));

		List<CartItemSimple> cartItems = new ArrayList<>();
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Burger Menü", (byte)0, 11.5, 1, null, cartItems_01));
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Pizza Menü", (byte)0, 11.5, 1, null, cartItems_02));

		
		CartSimple data = new CartSimple(false, 23, cartItems);
		
		Response response = new Response(200, null, data);
		return response;
	}
	
	@PUT
	@Path("{loginNr}/{id}")
	public Response removeItemFromCart(String loginNr, UUID id) {
		
		List<CartItemSimple> cartItems_01 = new ArrayList<>();
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Burger", (byte)0, 11.5, 1, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Pommes", (byte)0, 3.5, 2, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 3, null, null));
		
		List<CartItemSimple> cartItems_02 = new ArrayList<>();
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Pizza Magerita", (byte)0, 11.5, 1, null, null));
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 1, null, null));

		List<CartItemSimple> cartItems = new ArrayList<>();
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Burger Menü", (byte)0, 11.5, 1, null, cartItems_01));
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Pizza Menü", (byte)0, 11.5, 1, null, cartItems_02));

		
		CartSimple oldCart = new CartSimple(false, 23, cartItems);
		
		List<CartItemSimple> newCartItems = oldCart.cartItems().stream()//
			.map(cartItem -> {
				List<CartItemSimple> subItems = cartItem.subItems().stream()//
					.filter(cartSubItem -> cartSubItem.id() == id)
					.toList()
				;
				CartItemSimple newCartItem = new CartItemSimple(cartItem.id(), cartItem.displayName(), cartItem.image(), cartItem.price(), cartItem.count(), cartItem.extra(), subItems);
				return newCartItem;
			})
			.filter(cartItem -> cartItem.id() == id)
			.toList()
		;
		
		Response response = new Response(200, null, newCartItems);
		return response;
	}
	
	@PUT
	@Path("{loginNr}/{newPrio}")
	public Response changePrio(String loginNr, boolean newPrio) {
			
		List<CartItemSimple> cartItems_01 = new ArrayList<>();
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Burger", (byte)0, 11.5, 1, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Pommes", (byte)0, 3.5, 2, null, null));
		cartItems_01.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 3, null, null));
		
		List<CartItemSimple> cartItems_02 = new ArrayList<>();
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Pizza Magerita", (byte)0, 11.5, 1, null, null));
		cartItems_02.add(new CartItemSimple(UUID.randomUUID(), "Cola", (byte)0, 1.5, 1, null, null));

		List<CartItemSimple> cartItems = new ArrayList<>();
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Burger Menü", (byte)0, 11.5, 1, null, cartItems_01));
		cartItems.add(new CartItemSimple(UUID.randomUUID(), "Pizza Menü", (byte)0, 11.5, 1, null, cartItems_02));

		
		CartSimple data = new CartSimple(newPrio, 23, cartItems);
		
		Response response = new Response(200, null, data);
		return response;
	}

}
