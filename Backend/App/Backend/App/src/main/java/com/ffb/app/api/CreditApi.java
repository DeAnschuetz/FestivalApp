package com.ffb.app.api;

import com.ffb.app.service.api.api.credit.CreditService;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
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
	@Path("/by_login_nr/{loginNr}")
	@RolesAllowed("GUEST")
	public Response getCreditByLoginNr(@PathParam(value = "loginNr") String loginNr) throws ApiException {
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The login number must not be null.");
		}

		try {
			Credit credit =  creditService.getByLoginNr(loginNr);
			CreditResponse data = new CreditResponse(credit.getAmount());
			return Response.status(Response.Status.OK).entity(data).build();
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
	}

	@PUT
	@Path("add/by_login_nr/{loginNr}/{amount}")
	@RolesAllowed("GUEST")
	public Response addCredit(@PathParam(value = "loginNr") String loginNr, @PathParam(value = "amount") int amount) throws ApiException {// TODO Request?
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The login number must not be null.");
		}
		if (amount == 0) {
			throw new ApiException("The amount must not be 0.");
		}

		try {
			Credit credit =  creditService.changeAmount(loginNr, amount);
			CreditResponse data = new CreditResponse(credit.getAmount());
			return Response.status(Response.Status.OK).entity(data).build();
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
	}
}
