package com.ffb.app.api;




import com.ffb.app.service.api.credit.CreditService;
import com.ffb.app.service.impl.credit.CreditServiceImpl;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.response.Response;

import com.ffb.model.db.objects.credit.Credit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

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
		Credit credit =  creditService.findByLoginNr(loginNr);
		CreditResponse data = new CreditResponse(credit.getAmmount());
		Response response = new Response(200, null, data);
		return response;
	}
	
	@PUT
	@Path("{loginNr}")
	public Response addCredit(@PathParam(value = "loginNr") String loginNr, int amount) {
		Credit credit =  creditService.findByLoginNr(loginNr);
		CreditResponse data = new CreditResponse(credit.getAmmount());
		Response response = new Response(200, null, data);
		return response;
	}

}
