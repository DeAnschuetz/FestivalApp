package com.ffb.app.api;

import com.ffb.app.service.api.account.AccountService;
import com.ffb.model.api.request.ticket.TicketRequest;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.exception.ApiException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
@Path("ticket")
public class TicketApi {

    // TODO Logging
    private final Logger LOG = LoggerFactory.getLogger(TicketApi.class);

    private final AccountService accountService;

    @Inject
    public TicketApi(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Path("admin/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    @Operation(summary = "Register Tickets which were sold")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Tickets created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = TicketResponse.class,
                                    type = SchemaType.ARRAY
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = ErrorResponse.class,
                                    type = SchemaType.OBJECT
                            )
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response register(TicketRequest request) throws ApiException {
        if(request.loginNrs() == null || request.loginNrs().isEmpty()) {
            LOG.error("loginNrs is empty");
            throw new ApiException("LoginNrs must not be empty.", Response.Status.BAD_REQUEST);
        }
        LOG.info("Registering tickets for login numbers: " + request.loginNrs());

        List<TicketResponse> data = accountService.createTickets(request);

        LOG.info("Successfully registered tickets for login numbers: " + request.loginNrs());
        return Response.status(Response.Status.CREATED).entity(data).build();
    }

    @GET
    @Path("admin/list_all")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "List all Tickets")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "List of Tickets",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(
                                    implementation = TicketResponse.class,
                                    type = SchemaType.ARRAY
                            )
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response listAll() {
        List<TicketResponse> data = accountService.getAllTickets();
        return Response.status(Response.Status.OK).entity(data).build();
    }
}
