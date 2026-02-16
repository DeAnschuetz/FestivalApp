package com.ffb.api;

import java.util.List;

import org.jboss.resteasy.reactive.PartType;

import com.ffb.model.objects.account.Account;
import com.ffb.model.request.account.LoginRequest;
import com.ffb.model.response.Response;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/account")
public class AccountApi {
	
    @PUT
	public Response login(@PartType(MediaType.APPLICATION_JSON) LoginRequest loginRequest) {
		
		Response response = new Response(200, null, null);
		return response;
	}
	
    @POST
	public Response register(@PartType(MediaType.APPLICATION_JSON) LoginRequest registerRequest) {
		
    	Response response = new Response(200, null, null);
		return response;
	}
    
    @GET
	public Response getAll() {
    	List<Account> data = Account.listAll();
		Response response = new Response(200, null, data);
		return response;
	}
}
