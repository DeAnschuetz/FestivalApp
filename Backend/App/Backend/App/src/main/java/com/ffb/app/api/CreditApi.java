package com.ffb.api;

import com.ffb.model.response.Response;
import com.ffb.model.response.credit.CreditResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@ApplicationScoped
@Path("/credit")
public class CreditApi {
	
	@GET
	@Path("{loginNr}")
	public Response getCreditForLoginNr(@PathParam(value = "loginNr") String loginNr) {
		
		Response response = new Response(200, null, null);
		return response;
	}
	
	@PUT
	@Path("{loginNr}")
	public Response addCredit(@PathParam(value = "loginNr") String loginNr, int amount) {
		
		CreditResponse data = new CreditResponse(12.5);
		
		Response response = new Response(200, null, data);
		return response;
	}

}
