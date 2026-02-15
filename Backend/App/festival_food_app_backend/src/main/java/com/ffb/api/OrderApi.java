package com.ffb.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.PartType;

import com.ffb.model.objects.foodorder.FoodOrderStatus;
import com.ffb.model.request.order.ShareOrderRequest;
import com.ffb.model.response.OrderResponse;
import com.ffb.model.response.Response;
import com.ffb.model.response.order.OrderItemSimple;
import com.ffb.model.response.order.OrderSimple;

import io.quarkus.runtime.Application;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/order")
public class OrderApi {

	
	@POST
	@Path("{loginNr}")
	public Response shareOrder(@PathParam(value = "loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) ShareOrderRequest request) {
		
		Response response = new Response(200, null, null);
		return response;
	}
	
	@GET
	@Path("{loginNr}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderResponse getAll(@PathParam(value = "loginNr") String loginNr) {
		
		List<OrderSimple> data = new ArrayList<>();
		
		List<OrderItemSimple> orderItems_01 = new ArrayList<>();
		orderItems_01.add(new OrderItemSimple(UUID.randomUUID(), "Burger",  (byte)0, 1, null, null));
		orderItems_01.add(new OrderItemSimple(UUID.randomUUID(), "Pommes",  (byte)0, 2, null, null));
		orderItems_01.add(new OrderItemSimple(UUID.randomUUID(), "Cola",  (byte)0, 3, null, null));
		
		data.add(new OrderSimple(UUID.randomUUID(), FoodOrderStatus.IN_PROGRESS, "Burger Place", 10, orderItems_01));
		
		List<OrderItemSimple> orderItems_02 = new ArrayList<>();
		orderItems_02.add(new OrderItemSimple(UUID.randomUUID(), "Pizza Magerita",  (byte)0, 1, null, null));
		orderItems_02.add(new OrderItemSimple(UUID.randomUUID(), "Spaggeti Bollognese",  (byte)0, 2, null, null));
		orderItems_02.add(new OrderItemSimple(UUID.randomUUID(), "Cola",  (byte)0, 3, null, null));
		
		data.add(new OrderSimple(UUID.randomUUID(), FoodOrderStatus.ORDERED, "Pizza Place", 15, orderItems_02));
		
		OrderResponse response = new OrderResponse(200, null, data);

		return response;
	}
	
	@GET
	@Path("{loginNr}/{status}")
	public Response getByStatus(@PathParam(value = "loginNr") String loginNr, FoodOrderStatus status) {
		
		List<OrderItemSimple> orderItems = new ArrayList<>();
		orderItems.add(new OrderItemSimple(UUID.randomUUID(), "Burger",  (byte)0, 1, null, null));
		orderItems.add(new OrderItemSimple(UUID.randomUUID(), "Pommes",  (byte)0, 2, null, null));
		orderItems.add(new OrderItemSimple(UUID.randomUUID(), "Cola",  (byte)0, 3, null, null));
		
		OrderSimple data = new OrderSimple(UUID.randomUUID(), status, "Burger Place", 10, orderItems);
		
		Response response = new Response(200, null, data);
		return response;
	}

}
