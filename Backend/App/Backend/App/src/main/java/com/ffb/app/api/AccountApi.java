package com.ffb.app.api;

import java.util.List;
import com.ffb.app.service.api.credit.CreditService;
import com.ffb.model.api.request.account.RegisterRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.app.service.api.account.AccountService;
import com.ffb.model.api.request.account.LoginRequest;
import com.ffb.model.db.objects.account.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/account")
public class AccountApi {
	
	private final AccountService accountService;
	private final CreditService creditService;

	@Inject
	public AccountApi(AccountService accountService, CreditService creditService) {
		this.accountService = accountService;
		this.creditService = creditService;
	}

    @PUT
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(@PartType(MediaType.APPLICATION_JSON) LoginRequest loginRequest) {
		String loginNr = loginRequest.loginNr();
		if (loginNr == null| loginNr.isBlank()) {
			throw new WebApplicationException("");
		}
		String password = loginRequest.password();
		if (password == null | password.isBlank()) {
			throw new WebApplicationException("");
		}

		Boolean result = accountService.verifyAccount(loginRequest.loginNr(), loginRequest.password());
		Response response =  Response.status(Response.Status.BAD_REQUEST).entity("Wrong Login-Nr or Password").build();
		if (result) {
			response = Response.status(Response.Status.OK).entity("Successfully logged in").build();
		}
		return response;
	}

    @POST
	public Response register(@PartType(MediaType.APPLICATION_JSON) RegisterRequest registerRequest) {

		String loginNr = registerRequest.loginNr();
		if (loginNr == null| loginNr.isBlank()) {
			throw new WebApplicationException("");
		}
		String password = registerRequest.password();
		if (password == null | password.isBlank()) {
			throw new WebApplicationException("");
		}

    	Account createdAccount = accountService.createAccount(registerRequest.loginNr(), registerRequest.password());
		try {
			creditService.createInitialCredit(createdAccount);
			Response response =  Response.status(Response.Status.OK).entity(createdAccount).build();
			return response;
		} catch (IllegalStateException e) {
			throw new WebApplicationException(e);
		}

	}

    @GET
	public Response getAll() {
    	List<Account> data = accountService.getAllAccounts();
		Response response = Response.status(Response.Status.OK).entity(data).build();
		return response;
	}
}
