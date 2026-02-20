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
	public Response getCreditByLoginNr(@PathParam(value = "loginNr") String loginNr) {
		if (loginNr == null) {
			throw new WebApplicationException("The login number must not be null.");
		}

		try {
			Credit credit =  creditService.getByLoginNr(loginNr);
			CreditResponse data = new CreditResponse(credit.getAmount());
			return Response.status(Response.Status.OK).entity(data).build();
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
	}

	@PUT
	@Path("{loginNr}")
	public Response addCredit(@PathParam(value = "loginNr") String loginNr, int amount) {
		if (loginNr == null) {
			throw new WebApplicationException("The login number must not be null.");
		}
		if (amount == 0) {
			throw new WebApplicationException("The amount must not be 0.");
		}

		try {
			Credit credit =  creditService.changeAmount(loginNr, amount);
			CreditResponse data = new CreditResponse(credit.getAmount());
			return Response.status(Response.Status.OK).entity(data).build();
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
	}
}
