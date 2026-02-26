package com.ffb.app.api;

import com.ffb.app.service.api.api.credit.CreditService;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.math.BigDecimal;
import java.util.List;


@ApplicationScoped
@Path("credit")
public class CreditApi {

	@Inject
	JsonWebToken webToken;
	private final CreditService creditService;

	public CreditApi(CreditService creditService) {
		this.creditService = creditService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Get the current Credit")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Credit",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getCredit() throws ApiException {
		String loginNr = webToken.getName();

		CreditResponse data;
		try {
			data =  creditService.getByLoginNr(loginNr);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("add/{amount}")
	@RolesAllowed("GUEST")
	@Operation(summary = "Add a amount to the current Credit")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Credit",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addCredit(@PathParam(value = "amount") double amount) throws ApiException {// TODO Request?
		String loginNr = webToken.getName();
		if (amount < 0) {
			throw new ApiException("The amount must not be 0.", Response.Status.BAD_REQUEST);
		}

		CreditResponse data;
		try {
			data =  creditService.changeAmount(loginNr, amount);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{loginNr}/{pageIndex}/{pageSize}")
	@RolesAllowed("ADMIN")
	@Operation(summary = "Add a amount to the current Credit")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Current Credit History for the given LoginNr",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditHistoryResponse.class, type = SchemaType.ARRAY))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getHistoryByLoginNr(@PathParam("loginNr") String loginNr,@PathParam("pageIndex") int pageIndex,@PathParam("pageSize") int pageSize) throws ApiException {
		List<CreditHistoryResponse> data;
		try {
			data =  creditService.getHistoryByLoginNr(loginNr, pageIndex, pageSize);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	/*
		Private Helper Functions
	*/


	private CreditHistoryResponse getCreditHistoryResponse(CreditHistory creditHistory) {
		return new CreditHistoryResponse(
				creditHistory.getOldAmmount(),
				creditHistory.getNewAmmount(),
				creditHistory.getChangeTime()
		);
	}

}
