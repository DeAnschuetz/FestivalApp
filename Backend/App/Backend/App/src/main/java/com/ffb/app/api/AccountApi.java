package com.ffb.app.api;

import java.util.List;

import com.ffb.app.service.api.credit.CreditService;
import org.jboss.resteasy.reactive.PartType;

import com.ffb.app.service.api.account.AccountService;
import com.ffb.model.api.request.account.LoginRequest;
import com.ffb.model.api.response.response.Response;
import com.ffb.model.db.objects.account.Account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/account")
public class AccountApi {
	
	private final  AccountService accountService;
	private final CreditService creditService;
	
	@Inject
	public AccountApi(AccountService accountService, CreditService creditService) {
		this.accountService = accountService;
		this.creditService = creditService;
	}
	
    @PUT
	public Response login(@PartType(MediaType.APPLICATION_JSON) LoginRequest loginRequest) {
		Boolean result = accountService.verifyAccount(loginRequest.loginNr(), loginRequest.password());
		Response response = new Response(400, "Wrong Login-Nr or Password", null);
		if (result) {
			response = new Response(200, "Successfully logged in", null);
		}
		return response;
	}
	
    @POST
	public Response register(@PartType(MediaType.APPLICATION_JSON) LoginRequest registerRequest) {
    	Account createdAccount = accountService.createAccount(registerRequest.loginNr(), registerRequest.password());
		creditService.createInitialCredit(createdAccount);
    	Response response = new Response(200, null, createdAccount);
		return response;
	}
    
    @GET
	public Response getAll() {
    	List<Account> data = accountService.getAllAccounts();
		Response response = new Response(200, null, data);
		return response;
	}
}
