package com.ffb.app.api;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ffb.model.api.request.account.RegisterRequest;
import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.app.service.api.account.AccountService;
import com.ffb.model.api.request.account.LoginRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("account")
public class AccountApi {

	private final Logger LOG = LoggerFactory.getLogger(AccountApi.class);

	@Inject
	JsonWebToken jwt;
	private final AccountService accountService;

	@Inject
	public AccountApi(AccountService accountService) {
		this.accountService = accountService;
    }

	@POST
	@Path("login")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Operation(summary = "Login")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Login succeeded; cookie set",
					content = @Content(mediaType = MediaType.TEXT_PLAIN)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			)
	})
	public Response login(@PartType(MediaType.APPLICATION_JSON) LoginRequest loginRequest) throws ApiException {
		String loginNr = loginRequest.loginNr();
		LOG.info("Login attempt for loginNr={{}}", loginNr);
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("login number is null or blank.");
			throw new ApiException("The login number must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		if (loginRequest.password() == null || loginRequest.password().isBlank()) {
			LOG.error("password is null or blank.");
			throw new ApiException("The password must not be null or blank.", Response.Status.BAD_REQUEST);
		}

        String token;
        try {
            token = accountService.verifyAccount(loginRequest);
        } catch (ServiceException e) {
			LOG.error("Could not verify account={{}}; Exception: ", loginNr, e);
            throw new ApiException(e);
        }

		NewCookie cookie = new NewCookie.Builder("access_token")
				.value(token)//
				.path("/")//
				.httpOnly(true)//
				.secure(false)//
				.sameSite(NewCookie.SameSite.LAX)//
				.maxAge(2 * 60 * 60)//
				.build()//
		;

		LOG.info("login successful for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity("Successfully logged in: {" + loginNr +"}").cookie(cookie).build();
	}

	@POST
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "Logout")
	@APIResponses({
			@APIResponse(responseCode = "204", description = "Logout succeeded; access token cleared"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized")
	})
	public Response logout() {
		LOG.info("logging out loginNr={{}}", jwt.getName());
		NewCookie cleared = new NewCookie.Builder("access_token")//
				.value("")//
				.path("/")//
				.httpOnly(true)//
				.secure(true)//
				.maxAge(0)//
				.build()//
		;

		return Response.noContent().cookie(cleared).build();
	}

	@POST
	@Path("register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Operation(summary = "Register a new Account")
	@APIResponses({
			@APIResponse(
					responseCode = "201",
					description = "Account created",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			)
	})
	public Response register(@PartType(MediaType.APPLICATION_JSON) RegisterRequest registerRequest) throws ApiException {
		String loginNr = registerRequest.loginNr();
		LOG.trace("Register attempt for loginNr={{}}", loginNr);
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("Register attempt failed: login number is null or blank.");
			throw new ApiException("The login number must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		if (registerRequest.password() == null || registerRequest.password().isBlank()) {
			LOG.error("Register attempt failed: password is null or blank.");
			throw new ApiException("The password must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		Pattern loginNrPattern = Pattern.compile("[AFV][-]\\d{3}[-]\\d{3}[-]\\d{3}");
		Matcher matcher = loginNrPattern.matcher(loginNr);
		if (!matcher.find()) {
			LOG.error("loginNr={{}} has no valid format", loginNr);
			throw new ApiException("loginNr has no Valid Format", Response.Status.BAD_REQUEST);
		}

		AccountResponse createdAccount;
		try {
			createdAccount = accountService.createAccount(registerRequest);
		} catch (ServiceException e) {
			LOG.error("Register attempt failed for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(createdAccount).build();

	}

	@GET
	@Path("admin/list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed("ADMIN")
	@Operation(summary = "List all Accounts")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "List of Accounts",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = AccountResponse.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listAll() {
		LOG.info("listAll loginNr={{}}", jwt.getName());
		List<AccountResponse> data = accountService.getAllAccounts();
		LOG.info("found {} accounts", data.size());
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
