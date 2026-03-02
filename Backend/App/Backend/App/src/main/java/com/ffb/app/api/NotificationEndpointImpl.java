package com.ffb.app.api;

import com.ffb.app.service.api.notification.NotificationService;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.notification.FoodOrderNotificationResponse;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.db.object.notification.NotificationStatus;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@Path("notification")
public class NotificationEndpointImpl {

    private final Logger LOG = LoggerFactory.getLogger(NotificationEndpointImpl.class);

    @Inject
    JsonWebToken webToken;

    private final NotificationService notificationService;

    @Inject
    public NotificationEndpointImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GET
    @Path("list_all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"GUEST", "ADMIN"})
    @Transactional
    @Operation(summary = "List all Notifications visible to the currently logged-in Account (Role-Based)")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Notifications returned successfully",
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
        LOG.info("list all request for loginNr={{}}", webToken.getName());
        String loginNr = webToken.getName();
        AccountType accountType = getAccountType();
        List<FoodOrderNotificationResponse> data;
        try {
            data = notificationService.listByLoginNrAndAccountType(loginNr, accountType);
        } catch (ServiceException e) {
            LOG.error("could not list orders for loginNr={{}}; Exception: ", loginNr, e);
            throw new ApiException(e);
        }
        LOG.info("successfully listed orders for loginNr={{}}", loginNr);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @PUT
    @Path("update/{notificationId}/{newStatus}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"GUEST"})
    @Transactional
    @Operation(summary = "Update the Status of the Notification for the given ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Notifications returned successfully",
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
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response updateStatus(@PathParam(value = "notificationId")  UUID notificationId, @PathParam(value = "newStatus") NotificationStatus newStatus) throws ApiException {
        LOG.info("update notification status for id={{}}", notificationId);
        String loginNr = webToken.getName();
        FoodOrderNotificationResponse data;
        try {
            data = notificationService.setStatusById(notificationId, newStatus);
        } catch (ServiceException e) {
            LOG.error("could not list orders for loginNr={{}}; Exception: ", loginNr, e);
            throw new ApiException(e);
        }
        LOG.info("successfully listed orders for id={{}}", notificationId);
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
