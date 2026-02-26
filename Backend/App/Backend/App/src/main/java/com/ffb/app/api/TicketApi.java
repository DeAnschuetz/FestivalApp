package com.ffb.app.api;

import com.ffb.app.service.api.api.account.AccountService;
import com.ffb.app.service.api.api.token.TokenService;
import com.ffb.model.api.request.ticket.TicketRequest;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.db.objects.account.Ticket;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
@Path("ticket")
public class TicketApi {

    private final Logger LOG = Logger.getLogger(TicketApi.class);
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
    public Response register(TicketRequest request) throws ApiException {
        List<String> loginNrs = request.loginNrs();
        if(loginNrs == null || loginNrs.isEmpty()) {
            LOG.error("loginNrs is empty");
            throw new ApiException("LoginNrs must not be empty.", Response.Status.BAD_REQUEST);
        }
        LOG.info("Registering tickets for login numbers: " + loginNrs);

        List<TicketResponse> data;
        try {
            data = accountService.createTicket(loginNrs);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }

        LOG.info("Successfully registered tickets for login numbers: " + loginNrs);
        return Response.status(Response.Status.CREATED).entity(data).build();
    }

    @GET
    @Path("admin/list_all")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        List<TicketResponse> data = accountService.getAllTickets();
        return Response.status(Response.Status.OK).entity(data).build();
    }
}
