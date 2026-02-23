package com.ffb.app.api;

import com.ffb.app.service.api.api.credit.CreditService;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;


@ApplicationScoped
@Path("credit")
public class CreditApi {

	@Inject
	JsonWebToken jwt;
	private final CreditService creditService;

	public CreditApi(CreditService creditService) {
		this.creditService = creditService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	public Response getCredit() throws ApiException {
		String loginNr = jwt.getName();

		Credit credit;
		try {
			credit =  creditService.getByLoginNr(loginNr);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		CreditResponse data = new CreditResponse(credit.getAmount());
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add/{amount}")
	@RolesAllowed("GUEST")
	public Response addCredit(@PathParam(value = "amount") int amount) throws ApiException {// TODO Request?
		String loginNr = jwt.getName();
		if (amount == 0) {
			throw new ApiException("The amount must not be 0.", Response.Status.BAD_REQUEST);
		}

		Credit credit;
		try {
			credit =  creditService.changeAmount(loginNr, amount);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		CreditResponse data = new CreditResponse(credit.getAmount());
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
