package com.ffb.app.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.model.api.request.order.ShareOrderRequest;
import com.ffb.model.api.response.order.OrderItemSimple;
import com.ffb.model.api.response.order.OrderSimple;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
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

	
	@GET
	public Response getAll() {
		List<FoodOrder> data = FoodOrder.listAllWithItems();

		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("{loginNr}")
	public Response shareOrder(@PathParam(value = "loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) ShareOrderRequest request) {

		return Response.status(Response.Status.OK).entity(null).build();
	}

	@GET
	@Path("{loginNr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@PathParam(value = "loginNr") String loginNr) {

		List<OrderSimple> data = new ArrayList<>();
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("{loginNr}/{status}")
	public Response getByStatus(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "status") FoodOrderStatus status) {

		List<OrderItemSimple> orderItems = new ArrayList<>();
		OrderSimple data = new OrderSimple(UUID.randomUUID(), status, "Burger Place", 10, orderItems);
		return Response.status(Response.Status.OK).entity(data).build();
	}

}
