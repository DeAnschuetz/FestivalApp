package com.ffb.app.api;

import java.util.List;
import java.util.Set;

import com.ffb.app.service.api.api.token.TokenService;
import com.ffb.model.api.request.account.RegisterRequest;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.PartType;
import com.ffb.app.service.api.api.account.AccountService;
import com.ffb.model.api.request.account.LoginRequest;
import com.ffb.model.db.objects.account.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/account")
public class AccountApi {

	private final Logger LOG = Logger.getLogger(AccountApi.class);
	private final AccountService accountService;
	private final TokenService tokenService;

	@Inject
	public AccountApi(AccountService accountService, TokenService tokenService) {
		this.accountService = accountService;
        this.tokenService = tokenService;
    }

    @PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/login")
	@PermitAll
	public Response login(@PartType(MediaType.APPLICATION_JSON) LoginRequest loginRequest) throws ApiException {
		String loginNr = loginRequest.loginNr();
		LOG.info("Login attempt for loginNr: " + loginNr);
		if (loginNr == null || loginNr.isBlank()) {
			// TODO
			LOG.error("Login attempt failed: login number is null or blank.");
			throw new ApiException("The login number must not be null or blank.");
		}
		String password = loginRequest.password();
		if (password == null || password.isBlank()) {
			// TODO
			LOG.error("Login attempt failed: password is null or blank.");
			throw new ApiException("The password must not be null or blank.");
		}

        AccountType result = null;
        try {
            result = accountService.verifyAccount(loginNr, password);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }

		Set<String> roles = Set.of(result.toString());
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
		return Response.status(Response.Status.OK).cookie(cookie).build();
	}

	@POST
	@Path("/logout")
	public Response logout() {
		NewCookie cleared = new NewCookie.Builder("access_token")
				.value("")
				.path("/")
				.httpOnly(true)
				.secure(true)
				.maxAge(0)
				.build();

		return Response.noContent().cookie(cleared).build();
	}

	@Path("/register")
    @POST
	@PermitAll
	public Response register(@PartType(MediaType.APPLICATION_JSON) RegisterRequest registerRequest) throws ApiException {
		String loginNr = registerRequest.loginNr();
		LOG.trace("Register attempt for loginNr: " + loginNr);
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("Register attempt failed: login number is null or blank.");
			throw new ApiException("The login number must not be null or blank.");
		}
		String password = registerRequest.password();
		if (password == null || password.isBlank()) {
			LOG.error("Register attempt failed: password is null or blank.");
			throw new ApiException("The password must not be null or blank.");
		}

		try {
			Account createdAccount = accountService.createAccount(loginNr, password);
			return Response.status(Response.Status.OK).entity(createdAccount).build();
		} catch (ServiceException e) {
			LOG.error("Register attempt failed: " + e.getMessage());
			throw new ApiException(e);
		}

	}

    @GET
	@Path("/list_all")
	@RolesAllowed("ADMIN")
	public Response listAll() {
    	List<Account> data = accountService.getAllAccounts();
		Response response = Response.status(Response.Status.OK).entity(data).build();
		return response;
	}
}
