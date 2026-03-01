package com.ffb.app.api;

import com.ffb.app.service.api.credit.CreditService;
import com.ffb.app.validator.api.RequestValidator;
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
public class CreditEndpointImpl {

	// TODO Logging done fürs erste
	private final Logger LOG = LoggerFactory.getLogger(CreditEndpointImpl.class);

	@Inject
	JsonWebToken webToken;
	private final CreditService creditService;
	private final RequestValidator validator;


	public CreditEndpointImpl(CreditService creditService, RequestValidator validator) {
		this.creditService = creditService;
        this.validator = validator;
    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed("GUEST")
	@Operation(summary = "Get the Credit for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Credit for the currently logged-in Guest Account",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getCredit() throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("get credit request for loginNr={{}}", loginNr);

		CreditResponse data;
		try {
			data =  creditService.getByLoginNr(loginNr);
		} catch (ServiceException e) {
			LOG.error("could not get credit for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
		}
		LOG.info("successfully got credit for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("add")
	@RolesAllowed("GUEST")
	@Operation(summary = "Add a Amount to the Credit for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Credit for the currently logged-in Guest Account",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addCredit(@PartType(MediaType.APPLICATION_JSON) CreditAddRequest request) throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		try {
			validator.validateCreditAddRequest(request);
		} catch (ApiException e) {
			LOG.error("invalid request; Exception: ", e);
			throw e;
		}
		LOG.info("add credits request for loginNr={{}} with amount={}", loginNr, request.amount());

		CreditResponse data;
		try {
			data =  creditService.changeAmount(loginNr, request);
		} catch (ServiceException e) {
			LOG.error("could not add credits for loginNr={{}} with amount={}; Exception: ", loginNr, request.amount(), e);
			throw new ApiException(e);
		}
		LOG.info("successfully added credits for loginNr={{}} with amount={}", loginNr, request.amount());
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("history")
	@RolesAllowed("ADMIN")
	@Operation(summary = "Get the Credit History for the given LoginNr")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Credit History for the given LoginNr",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditHistoryResponse.class, type = SchemaType.ARRAY))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getHistoryByLoginNr(@PartType(MediaType.APPLICATION_JSON) CreditHistoryRequest request) throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		try {
			validator.validateCreditHistoryRequest(request);
		} catch (ApiException e) {
			LOG.error("invalid request; Exception: ", e);
			throw e;
		}
		LOG.info("get credit history request for loginNr={{}}", loginNr);

		List<CreditHistoryResponse> data =  creditService.getHistoryByLoginNr(request);
		LOG.info("successfully got credit history for loginNr={{}}", loginNr);
        return Response.status(Response.Status.OK).entity(data).build();
	}
}
