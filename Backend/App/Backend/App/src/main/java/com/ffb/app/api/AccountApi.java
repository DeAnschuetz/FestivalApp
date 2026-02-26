package com.ffb.app.api;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ffb.app.service.api.api.token.TokenService;
import com.ffb.model.api.request.account.RegisterRequest;
import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.account.LoginResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.app.service.api.api.account.AccountService;
import com.ffb.model.api.request.account.LoginRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("account")
public class AccountApi {

	private final Logger LOG = Logger.getLogger(AccountApi.class);
	private final AccountService accountService;
	private final TokenService tokenService;

	@Inject
	public AccountApi(AccountService accountService, TokenService tokenService) {
		this.accountService = accountService;
        this.tokenService = tokenService;
    }

	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	@Operation(summary = "Login")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Login succeeded; cookie set",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LoginResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			)
	})
	public Response login(@PartType(MediaType.APPLICATION_JSON) LoginRequest loginRequest) throws ApiException {
		String loginNr = loginRequest.loginNr();
		LOG.info("Login attempt for loginNr: " + loginNr);
		if (loginNr == null || loginNr.isBlank()) {
			// TODO
			LOG.error("Login attempt failed: login number is null or blank.");
			throw new ApiException("The login number must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		String password = loginRequest.password();
		if (password == null || password.isBlank()) {
			// TODO
			LOG.error("Login attempt failed: password is null or blank.");
			throw new ApiException("The password must not be null or blank.", Response.Status.BAD_REQUEST);
		}

        AccountType type;
        try {
            type = accountService.verifyAccount(loginNr, password);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }

		Set<String> roles = Set.of(type.toString());
		String jwt = tokenService.createToken(loginNr, roles);

		NewCookie cookie = new NewCookie.Builder("access_token")
				.value(jwt)//
				.path("/")//
				.httpOnly(true)//
				.secure(false)//
				.sameSite(NewCookie.SameSite.LAX)//
				.maxAge(2 * 60 * 60)//
				.build()//
		;

		LOG.info("Login successful for loginNr: " + loginNr);
		return Response.status(Response.Status.OK).entity((new LoginResponse(type))).cookie(cookie).build();
	}

	@POST
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "Logout")
	@APIResponse(responseCode = "204", description = "Logout succeeded; access token cleared")
	@APIResponse(
			responseCode = "400",
			description = "Invalid request",
			content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
	)
	public Response logout() {
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
		LOG.trace("Register attempt for loginNr: " + loginNr);
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("Register attempt failed: login number is null or blank.");
			throw new ApiException("The login number must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		String password = registerRequest.password();
		if (password == null || password.isBlank()) {
			LOG.error("Register attempt failed: password is null or blank.");
			throw new ApiException("The password must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		Pattern loginNrPattern = Pattern.compile("[AFV][-]\\d{3}[-]\\d{3}[-]\\d{3}");
		Matcher matcher = loginNrPattern.matcher(loginNr);
		if (!matcher.find()) {
			throw new ApiException("loginNr has no Valid Format", Response.Status.BAD_REQUEST);
		}

		AccountResponse createdAccount;
		try {
			createdAccount = accountService.createAccount(loginNr, password);
		} catch (ServiceException e) {
			LOG.error("Register attempt failed: " + e.getMessage());
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
    	List<AccountResponse> data = accountService.getAllAccounts();
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
