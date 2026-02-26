package com.ffb.app.api;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.ffb.app.service.api.api.food.order.FoodOrderService;
import com.ffb.model.api.response.order.FoodOrderItemResponse;
import com.ffb.model.api.response.order.FoodOrderResponse;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.model.api.request.food.order.ShareOrderRequest;
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
        List<FoodOrderResponse> data;
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
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Transactional
	public Response listAll() throws ApiException {
		String loginNr = jwt.getName();
		Set<String> groups = jwt.getGroups();
		AccountType accountType;
		if (groups.contains("ADMIN")) {
			accountType = AccountType.ADMIN;
		} else if (groups.contains("FOOD_COURT_WORKER")) {
			accountType = AccountType.FOOD_COURT_WORKER;
		} else if (groups.contains("GUEST")) {
			accountType = AccountType.GUEST;
		} else {
			throw new ApiException("Unknown AccountType: " + groups, Response.Status.BAD_REQUEST);
		}
        List<FoodOrderResponse> data;
        try {
            data = foodOrderService.listByLoginNrAndAccountType(loginNr, accountType, false);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("list_all/by_status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	public Response listByStatus(@PathParam(value = "status") FoodOrderStatus status) throws ApiException {
		String loginNr = jwt.getName();
		Set<String> groups = jwt.getGroups();
		AccountType accountType;
		if (groups.contains("ADMIN")) {
			accountType = AccountType.ADMIN;
		} else if (groups.contains("FOOD_COURT_WORKER")) {
			accountType = AccountType.FOOD_COURT_WORKER;
		} else if (groups.contains("GUEST")) {
			accountType = AccountType.GUEST;
		} else {
			throw new ApiException("Unknown AccountType: " + groups, Response.Status.BAD_REQUEST);
		}
		List<FoodOrderResponse> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountTypeAndStatus(loginNr, accountType, status, false);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}


	@GET
	@Path("list_all/items")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	public Response listAllWithItems() throws ApiException {
		String loginNr = jwt.getName();
		Set<String> groups = jwt.getGroups();
		AccountType accountType;
		if (groups.contains("ADMIN")) {
			accountType = AccountType.ADMIN;
		} else if (groups.contains("FOOD_COURT_WORKER")) {
			accountType = AccountType.FOOD_COURT_WORKER;
		} else if (groups.contains("GUEST")) {
			accountType = AccountType.GUEST;
		} else {
			throw new ApiException("Unknown AccountType: " + groups, Response.Status.BAD_REQUEST);
		}
		List<FoodOrderResponse> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountType(loginNr, accountType, false);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("list_all/by_status/{status}/items")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	public Response listByStatusWithItems(@PathParam(value = "status") FoodOrderStatus status) throws ApiException {
		String loginNr = jwt.getName();
		Set<String> groups = jwt.getGroups();
		AccountType accountType;
		if (groups.contains("ADMIN")) {
			accountType = AccountType.ADMIN;
		} else if (groups.contains("FOOD_COURT_WORKER")) {
			accountType = AccountType.FOOD_COURT_WORKER;
		} else if (groups.contains("GUEST")) {
			accountType = AccountType.GUEST;
		} else {
			throw new ApiException("Unknown AccountType: " + groups, Response.Status.BAD_REQUEST);
		}
		List<FoodOrderResponse> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountTypeAndStatus(loginNr, accountType, status, false);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
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

        FoodOrderResponse data;
        try {
			data = foodOrderService.updateStatus(orderId, status);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

}
