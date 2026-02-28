package com.ffb.app.api;

import com.ffb.app.service.api.credit.CreditService;
import com.ffb.model.api.request.credit.CreditAddRequest;
import com.ffb.model.api.request.credit.CreditHistoryRequest;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.error.ErrorResponse;
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
import org.jboss.resteasy.reactive.PartType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@ApplicationScoped
@Path("credit")
public class CreditApi {

	// TODO Logging
	private final Logger LOG = LoggerFactory.getLogger(CreditApi.class);

	@Inject
	JsonWebToken webToken;
	private final CreditService creditService;

	public CreditApi(CreditService creditService) {
		this.creditService = creditService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("add")
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
	public Response addCredit(@PartType(MediaType.APPLICATION_JSON) CreditAddRequest request) throws ApiException {
		String loginNr = webToken.getName();
		if (request.amount() < 0) {
			throw new ApiException("The amount must not be 0.", Response.Status.BAD_REQUEST);
		}

		CreditResponse data;
		try {
			data =  creditService.changeAmount(loginNr, request);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("history")
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
	public Response getHistoryByLoginNr(@PartType(MediaType.APPLICATION_JSON) CreditHistoryRequest request) throws ApiException {
		if (request.loginNr() == null || request.loginNr().isBlank()) {
			throw new ApiException("Login NR is required.", Response.Status.BAD_REQUEST);
		}
		if(request.pageIndex() < 0) {
			throw new ApiException("Page index is required.", Response.Status.BAD_REQUEST);
		}
		if(request.pageSize() < 0) {
			throw  new ApiException("Page size is required.", Response.Status.BAD_REQUEST);
		}

		List<CreditHistoryResponse> data;
		try {
			data =  creditService.getHistoryByLoginNr(request);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
