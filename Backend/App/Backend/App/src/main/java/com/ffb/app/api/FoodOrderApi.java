package com.ffb.app.api;

import java.util.List;
import java.util.UUID;

import com.ffb.app.service.api.food.order.FoodOrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.model.api.request.order.ShareOrderRequest;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/food_order")
public class FoodOrderApi {

	private final FoodOrderService foodOrderService;

	@Inject
	public FoodOrderApi(FoodOrderService foodOrderService) {
		this.foodOrderService = foodOrderService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list/all")
	public Response listAll() {
		List<FoodOrder> data = foodOrderService.listAll(true);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("/list/by_login_nr/{loginNr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listByLoginNr(@PathParam(value = "loginNr") String loginNr) {
		if(loginNr == null || loginNr.isEmpty()) {
			// TODO
			throw new WebApplicationException("", Response.Status.BAD_REQUEST);
		}
		List<FoodOrder> data = foodOrderService.listByLoginNr(loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("/list/by_login_nr_and_status/{loginNr}/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listByStatus(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "status") FoodOrderStatus status) {
		if(loginNr == null || loginNr.isEmpty()) {
			// TODO
			throw new WebApplicationException("", Response.Status.BAD_REQUEST);
		}
		if(status == null) {
			// TODO
			throw new WebApplicationException("", Response.Status.BAD_REQUEST);
		}

		List<FoodOrder> data = foodOrderService.listByLoginNrAndStatus(loginNr, status);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/order/{loginNr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response order(@PathParam(value = "loginNr") String loginNr) {
		if(loginNr == null || loginNr.isEmpty()) {
			// TODO
			throw new WebApplicationException("", Response.Status.BAD_REQUEST);
		}
		List<FoodOrder> data = foodOrderService.create(loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("/share/{loginNr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response shareOrder(@PathParam(value = "loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) ShareOrderRequest request) {
		UUID orderId = request.orderId();
		if(orderId == null) {
			// TODO
			throw new WebApplicationException("Order ID is required", Response.Status.BAD_REQUEST);
		}
		String sharedLoginNr = request.loginNr();
		if(sharedLoginNr == null || sharedLoginNr.isEmpty()) {
			// TODO
			throw  new WebApplicationException("Login ID is required", Response.Status.BAD_REQUEST);
		}

		foodOrderService.shareOrder(loginNr, orderId, sharedLoginNr);
		return Response.status(Response.Status.OK).entity(null).build();
	}

	@PUT
	@Path("/update/{orderId}/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrderStatus(@PathParam(value = "orderId") UUID orderId, @PathParam(value = "status") FoodOrderStatus status) {
		if(orderId == null) {
			// TODO
			throw  new WebApplicationException("", Response.Status.BAD_REQUEST);
		}
		if (status == null) {
			// TODO
			throw new WebApplicationException("", Response.Status.BAD_REQUEST);
		}

		FoodOrder data = foodOrderService.updateStatus(orderId, status);
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
