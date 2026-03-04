package com.ffb.app.api;

import com.ffb.app.service.api.food.order.FoodOrderService;
import com.ffb.app.validator.api.RequestValidator;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponseFull;
import com.ffb.model.api.request.food.order.ShareOrderRequest;
import com.ffb.model.api.response.food.order.FoodOrderResponseHistory;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.CustomRuntimeException;
import com.ffb.model.exception.ServiceException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import org.jboss.resteasy.reactive.PartType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@Path("food_order")
public class FoodOrderEndpointImpl {

	// TODO Logging done fürs erste
	private final Logger LOG = LoggerFactory.getLogger(FoodOrderEndpointImpl.class);

	@Inject
	JsonWebToken webToken;
	private final FoodOrderService foodOrderService;
	private final RequestValidator validatorService;

	@Inject
	public FoodOrderEndpointImpl(FoodOrderService foodOrderService, RequestValidator validatorService) {
		this.foodOrderService = foodOrderService;
        this.validatorService = validatorService;
    }

	@POST
	@Path("order")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Create a new Food Order for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Order created successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = FoodOrderResponse.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required Resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "500",
					description = "Service Error while creating the Order",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response order() throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("order request for loginNr={{}}", loginNr);
		List<FoodOrderResponse> data;
		try {
			data = foodOrderService.create(loginNr);
		} catch (ServiceException  e) {
			LOG.error("could not create order; Exception: ", e);
			throw new ApiException(e);
		} catch (CustomRuntimeException e) {
			LOG.error("could not create order; Exception: ", e);
			throw new ApiException(e);
		}
		LOG.info("order request successful for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("share")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Share an existing Food Order with another Guest Account (by loginNr)")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Order shared successfully"
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required Resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response shareOrder(@PartType(MediaType.APPLICATION_JSON) ShareOrderRequest request) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("share order request for loginNr={{}}", loginNr);
		if(request.orderId() == null) {
			LOG.error("orderId is null");
			throw new ApiException("Order ID is required.", Response.Status.BAD_REQUEST);
		}
		if(request.loginNr() == null || request.loginNr().isBlank()) {
			LOG.error("loginNr is null or blank");
			throw  new ApiException("Login ID is required.", Response.Status.BAD_REQUEST);
		}

		try {
			foodOrderService.shareOrder(loginNr, request);
		} catch (ServiceException e) {
			LOG.error("could not share order; Exception: ", e);
			throw new ApiException(e);
		}
		LOG.info("successfully shared order for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity(null).build();
	}

	@GET
	@Path("list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Transactional
	@Operation(summary = "List all Food Orders visible to the currently logged-in Account (Role-Based)")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Orders returned successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = FoodOrderResponse.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Unknown Account Type / Role mapping failed",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required Resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listAll() throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("list all request for loginNr={{}}", loginNr);
		AccountType accountType = getAccountType();
		List<FoodOrderResponse> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountType(loginNr, accountType);
		} catch (ServiceException e) {
			LOG.error("could not list orders for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
		}
		LOG.info("successfully listed orders for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("list_all/by_status/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "List all Food Orders visible to the currently logged-in Account (Role-Based) filtered by Status")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Orders returned successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = FoodOrderResponse.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Unknown Account Type / Role mapping failed",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required Resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listByStatus(@PathParam(value = "status") FoodOrderStatus status) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("list all request for loginNr={{}} and status='{}'", loginNr, status);
		AccountType accountType = getAccountType();
		List<FoodOrderResponse> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountTypeAndStatus(loginNr, accountType, status);
		} catch (ServiceException e) {
			LOG.error("could not list orders for loginNr={{}} and status='{}'; Exception: ", loginNr, status, e);
			throw new ApiException(e);
		}
		LOG.info("successfully listed orders for loginNr={{}} and status='{}'", loginNr, status);
		return Response.status(Response.Status.OK).entity(data).build();
	}


	@GET
	@Path("list_all/history")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "List all Food Orders, History included visible to the currently logged-in Account (Role-Based)")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Orders returned successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = FoodOrderResponseHistory.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Unknown account Type / Role mapping failed",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required Resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listAllWithHistory() throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("list all with history request for loginNr={{}}", loginNr);
		AccountType accountType = getAccountType();
		List<FoodOrderResponseHistory> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountTypeWithHistory(loginNr, accountType);
		} catch (ServiceException e) {
			LOG.error("could not list orders with history for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
		}
		LOG.info("successfully listed orders for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("list_all/by_status/{status}/history")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "List all Food Orders visible to the currently logged-in Account (Role-Based) filtered by Status with History")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Orders returned successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = FoodOrderResponseHistory.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Status or unknown Account Type",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required Resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listByStatusWithHistory(@PathParam(value = "status") FoodOrderStatus status) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("list all with history request for loginNr={{}} and status='{}'", loginNr, status);
		AccountType accountType = getAccountType();
		List<FoodOrderResponseHistory> data;
		try {
			data = foodOrderService.listByLoginNrAndAccountTypeAndStatusWithHistory(loginNr, accountType, status);
		} catch (ServiceException e) {
			LOG.error("could not list orders with history for loginNr={{}} and status='{}'; Exception: ", loginNr, status, e);
			throw new ApiException(e);
		}
		LOG.info("successfully listed orders with history for loginNr={{}} and status='{}'", loginNr, status);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("update/{orderId}/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Update the Status of an Food Order by its ID")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Order Status updated",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = FoodOrderResponseFull.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Some required resource was not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response updateOrderStatus(@PathParam(value = "orderId") UUID orderId, @PathParam(value = "status") FoodOrderStatus newStatus) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("received update order status request for loginNr={{}} and orderId={{}} and newStatus='{}'", loginNr, orderId, newStatus);
		if(orderId == null) {
			LOG.error("orderId is null");
			throw new ApiException("Order ID is required.", Response.Status.BAD_REQUEST);
		}
		if (newStatus == null) {
			LOG.error("newStatus is null");
			throw new ApiException("Status is required.", Response.Status.BAD_REQUEST);
		}

        FoodOrderResponse data;
        try {
			data = foodOrderService.updateStatus(orderId, newStatus);
        } catch (ServiceException e) {
			LOG.error("could not update order for orderId={{}} and newStatus='{}'; Exception: ", orderId, newStatus, e);
            throw new ApiException(e);
        }
		LOG.info("successfully updated order for orderId={{}} and newStatus='{}'", orderId, newStatus);
        return Response.status(Response.Status.OK).entity(data).build();
	}

	/*
    	Private Helper Functions
	*/

	private AccountType getAccountType() throws ApiException {
		Set<String> groups = webToken.getGroups();
		AccountType accountType;
		if (groups.contains("ADMIN")) {
			accountType = AccountType.ADMIN;
		} else if (groups.contains("FOOD_COURT_WORKER")) {
			accountType = AccountType.FOOD_COURT_WORKER;
		} else if (groups.contains("GUEST")) {
			accountType = AccountType.GUEST;
		} else {
			LOG.error("unknown account type");
			throw new ApiException("Unknown AccountType: " + groups, Response.Status.BAD_REQUEST);
		}
		LOG.info("account is {}", accountType);
		return accountType;
	}
}
