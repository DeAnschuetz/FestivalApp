package com.ffb.app.api;

import com.ffb.app.service.api.api.account.AccountService;
import com.ffb.app.service.api.api.token.TokenService;
import com.ffb.model.api.request.ticket.TicketRequest;
import com.ffb.model.db.objects.account.Ticket;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
    @Path("register")
    @RolesAllowed("ADMIN")
    public Response register(TicketRequest request) throws ApiException {
        List<String> loginNrs = request.loginNrs();
        LOG.info("Registering tickets for login numbers: " + loginNrs);

        List<Ticket> data;
        try {
            data = accountService.createTicket(loginNrs);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }

        LOG.info("Successfully registered tickets for login numbers: " + loginNrs);
        return Response.status(Response.Status.CREATED).entity(data).build();
    }

    @GET
    @Path("list_all")
    @RolesAllowed("ADMIN")
    public Response listAll() {
        List<Ticket> data = accountService.getAllTickets();
        return Response.status(Response.Status.OK).entity(data).build();
    }
}
