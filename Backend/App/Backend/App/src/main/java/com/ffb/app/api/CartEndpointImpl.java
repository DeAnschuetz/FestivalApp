package com.ffb.app.api;

import com.ffb.app.service.api.cart.CartService;
import com.ffb.app.validator.api.RequestValidator;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.response.cart.CartResponseFull;
import com.ffb.model.api.response.cart.CartResponseSimple;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("cart")
public class CartEndpointImpl {

	// TODO Logging done fürs erste
	private static final Logger LOG = LoggerFactory.getLogger(CartEndpointImpl.class);

	@Inject
	JsonWebToken webToken;
	private final CartService cartService;
	private final RequestValidator validator;

	@Inject
	public CartEndpointImpl(CartService cartService, RequestValidator validator) {
		this.cartService = cartService;
        this.validator = validator;
    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@RolesAllowed({"GUEST"})
	@Operation(summary = "Get the Cart for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getCart() throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("get cart request for loginNr={{}}", webToken.getName());

        CartResponseSimple data;
        try {
            data = cartService.getCartByLoginNr(loginNr);
        } catch (ServiceException e) {
			LOG.error("could not get cart for loginNr={{}}; Exception: ", loginNr, e);
            throw new ApiException(e);
        }
		LOG.info("successfully got cart for loginNr={{}}", loginNr);
        return Response.ok().entity(data).build();

	}

	@PUT
	@Path("add_cart_item")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Add a Item to the Cart of the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addItemToCart(CartItemCreationRequest request ) throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("add cart item request for loginNr={{}}", webToken.getName());

		try {
			validator.validateItemCreationRequest(request);
		} catch (ApiException e) {
			LOG.error("invalid add cart item request for loginNr={{}}; Exception: ", loginNr, e);
			throw e;
		}

		CartResponseSimple data;
        try {
            data = cartService.addItemToCart(loginNr, request);
        } catch (ServiceException e) {
			LOG.error("could not add cart item for loginNr={{}} and productId={{}}; Exception: ", loginNr, request.productId(), e);
			throw new ApiException(e);
        }
		LOG.info("successfully added cart item for loginNr={{}} and productId={{}}", loginNr, request.productId());
		return Response.ok().entity(data).build();
	}

	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Update the Amount or Extra of a Cart Item for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response updateCartItem(CartItemUpdateRequest request ) throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("update cart item request for loginNr={{}}", webToken.getName());

		try {
			validator.validateUpdateRequest(request);
		} catch (ApiException e) {
			LOG.error("invalid update cart item request for loginNr={{}}; Exception: ", loginNr, e);
			throw e;
		}

		CartResponseSimple data;
        try {
            data = cartService.updateCartItemById(loginNr, request);
        } catch (ServiceException e) {
			LOG.error("could not update cart item for loginNr={{}} and cartItemId={{}}; Exception: ", loginNr, request.cartItemId(), e);

			throw new ApiException(e);
        }
		LOG.info("successfully updated cart item for loginNr={{}} and cartItemId={{}}", loginNr, request.cartItemId());
        return Response.ok().entity(data).build();
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Remove a Item from the Cart for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response removeItemFromCart(@PathParam(value = "id")  UUID id) throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if(id == null) {
			LOG.error("cartItemId is null");
			throw new ApiException("Cart item id must be provided", Response.Status.BAD_REQUEST);
		}
		LOG.info("remove cart item request for loginNr={{}} and cartItemId={{}}", webToken.getName(), id);

		CartResponseSimple data;
		try {
			data = cartService.removeItemFromCart(loginNr, id);
		} catch (ServiceException e) {
			LOG.error("could not remove cart item for loginNr={{}} and cartItemId={{}}; Exception: ", loginNr, id, e);
			throw new ApiException(e);
		}
		LOG.info("successfully removed cart item for loginNr={{}} and cartItemId={{}}", loginNr, id);
		return Response.ok().entity(data).build();
	}

	@PUT
	@Path("{newPriority}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Change the Priority of the Cart for the currently logged-in Guest Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Cart for the currently logged-in Guest Account",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseSimple.class, type = SchemaType.OBJECT)
					)
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = ErrorResponse.class)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response changePriority(@PathParam(value = "newPriority") boolean newPriority) throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("change cart priority request for loginNr={{}} and newPriority={}", webToken.getName(), newPriority);

		CartResponseSimple data;
        try {
            data = cartService.changePrio(loginNr, newPriority);
        } catch (ServiceException e) {
			LOG.error("could not change cart priority for loginNr={{}} and newPriority={}; Exception: ", loginNr, newPriority, e);
			throw new ApiException(e);
        }
		LOG.info("successfully changed cart priority for loginNr={{}} and newPriority={}", loginNr, newPriority);
		return Response.ok().entity(data).build();
	}

	@GET
	@Path("list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Get all the Carts")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "All the Carts",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON,
							schema = @Schema(implementation = CartResponseFull.class, type = SchemaType.ARRAY)
					)
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listALl() throws ApiException {
		String loginNr;
		try {
			loginNr = validator.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("list all carts request for loginNr={{}}", loginNr);
		List<CartResponseFull> data = cartService.listAll();
		LOG.info("successfully listed all carts (count={})", data.size());
		return Response.ok().entity(data).build();
	}

}
