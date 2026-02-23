package com.ffb.app.api;

import java.util.List;
import java.util.UUID;

import com.ffb.app.service.api.api.food.order.FoodOrderService;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.model.api.request.order.ShareOrderRequest;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("food_order")
public class FoodOrderApi {

	@Inject
	JsonWebToken jwt;
	private final FoodOrderService foodOrderService;

	@Inject
	public FoodOrderApi(FoodOrderService foodOrderService) {
		this.foodOrderService = foodOrderService;
	}

	@POST
	@Path("order")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	public Response order() throws ApiException {
		String loginNr = jwt.getName();
        List<FoodOrder> data;
        try {
            data = foodOrderService.create(loginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("share")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	public Response shareOrder(@PartType(MediaType.APPLICATION_JSON) ShareOrderRequest request) throws ApiException {
		String loginNr = jwt.getName();
		UUID orderId = request.orderId();
		if(orderId == null) {
			throw new ApiException("Order ID is required.", Response.Status.BAD_REQUEST);
		}
		String sharedLoginNr = request.loginNr();
		if(sharedLoginNr == null || sharedLoginNr.isEmpty()) {
			throw  new ApiException("Login ID is required.", Response.Status.BAD_REQUEST);
		}

        try {
            foodOrderService.shareOrder(loginNr, orderId, sharedLoginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(null).build();
	}

	@GET
	@Path("list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER"})
	public Response listAll() throws ApiException {
		String loginNr = jwt.getName();
		List<FoodOrder> data = foodOrderService.listByLoginNr(loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("list_all/by_status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER"})
	public Response listByStatus(@PathParam(value = "status") FoodOrderStatus status) throws ApiException {
		String loginNr = jwt.getName();
		if(status == null) {
			throw new ApiException("Status must be provided.", Response.Status.BAD_REQUEST);
		}

		List<FoodOrder> data = foodOrderService.listByLoginNrAndStatus(loginNr, status);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("update/{orderId}/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response updateOrderStatus(@PathParam(value = "orderId") UUID orderId, @PathParam(value = "status") FoodOrderStatus status) throws ApiException {
		if(orderId == null) {
			throw new ApiException("Order ID is required.", Response.Status.BAD_REQUEST);
		}
		if (status == null) {
			throw new ApiException("Status is required.", Response.Status.BAD_REQUEST);
		}

        FoodOrder data;
        try {
            data = foodOrderService.updateStatus(orderId, status);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("admin/list_all")
	@RolesAllowed("ADMIN")
	public Response listAllAdmin() {
		List<FoodOrder> data = foodOrderService.listAll(true);
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
