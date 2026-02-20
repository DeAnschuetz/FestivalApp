package com.ffb.app.api;

import com.ffb.app.service.api.credit.CreditService;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.db.objects.credit.Credit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;


@ApplicationScoped
@Path("/credit")
public class CreditApi {

	private final CreditService creditService;

	public CreditApi(CreditService creditService) {
		this.creditService = creditService;
	}

	@GET
	@Path("{loginNr}")
	public Response getCreditForLoginNr(@PathParam(value = "loginNr") String loginNr) {
		try {
			Credit credit =  creditService.getByLoginNr(loginNr);
			CreditResponse data = new CreditResponse(credit.getAmmount());
			Response response =  Response.status(Response.Status.OK).entity(data).build();
			return response;
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
	}

	@PUT
	@Path("{loginNr}")
	public Response addCredit(@PathParam(value = "loginNr") String loginNr, int amount) {
		try {
			Credit credit =  creditService.addAmount(loginNr, amount);
			CreditResponse data = new CreditResponse(credit.getAmmount());
			Response response =  Response.status(Response.Status.OK).entity(data).build();
			return response;
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
	}
}
