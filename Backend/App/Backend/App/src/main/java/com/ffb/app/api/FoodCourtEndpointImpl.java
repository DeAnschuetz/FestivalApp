package com.ffb.app.api;

import com.ffb.app.service.api.food.court.FoodCourtService;
import com.ffb.app.validator.api.RequestValidator;
import com.ffb.model.api.request.food.court.FoodCourtRequestSimple;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.request.food.court.FoodCourtRequest;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.security.RolesAllowed;
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
import org.jboss.resteasy.reactive.RestForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("food_court")
public class FoodCourtEndpointImpl {

	// TODO Logging done furs erste
	private final Logger LOG = LoggerFactory.getLogger(FoodCourtEndpointImpl.class);

	@Inject
	JsonWebToken webToken;
	private final FoodCourtService foodCourtService;
	private final RequestValidator validatorService;

	@Inject
	public FoodCourtEndpointImpl(FoodCourtService foodCourtService, RequestValidator validatorService) {
		this.foodCourtService = foodCourtService;
        this.validatorService = validatorService;
    }

	@GET
	@Path("list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "ADMIN"})
	@Operation(summary = "Lists all Food Courts")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "List of all Food Courts",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.ARRAY))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listAll() throws ApiException {		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("list all food courts request for loginNr={}}", loginNr);

		List<FoodCourtResponse> data = foodCourtService.listAll();

		LOG.info("successfully listed all food courts; count={}", data.size());
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("by_id/{foodCourtId}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "ADMIN"})
	@Operation(summary = "Get a Food Court By its ID")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Food Court with the given ID",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class))
			),
			@APIResponse(
					responseCode = "404",
					description = "Food Court not found",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getById(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if (foodCourtId == null) {
			LOG.error("foodCourtId is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}
		LOG.info("get food court request for loginNr={{}} and foodCourtId={{}}", loginNr, foodCourtId);


		FoodCourtResponse data;
        try {
			data = foodCourtService.get(foodCourtId);
        } catch (ServiceException e) {
			LOG.error("could not get food court; foodCourtId={{}}; Exception: ", foodCourtId, e);
			throw new ApiException(e);
        }

		LOG.info("successfully got food court; foodCourtId={{}}", foodCourtId);
		return Response.status(Response.Status.OK).entity(data).build();
    }

	@GET
	@Produces("image/png")
	@Path("image/{foodCourtId}")
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "Get the Food Court Image by Food Court ID (PNG)")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "PNG image bytes",
					content = @Content(mediaType = "image/png", schema = @Schema(type = SchemaType.STRING, format = "binary"))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Image not found",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getImageById(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if(foodCourtId == null) {
			LOG.error("foodCourtId is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}
		LOG.info("get food court image request for loginNr={{}} foodCourtId={}", loginNr, foodCourtId);

		byte[] imageBytes;
		try {
			imageBytes = foodCourtService.getImage(foodCourtId);
		} catch (ServiceException e) {
			LOG.error("could not get food court image; foodCourtId={{}}; Exception: ", foodCourtId, e);
			throw new ApiException(e);
		}

		if (imageBytes == null) {
			LOG.error("food court image not found; foodCourtId={{}}", foodCourtId);
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		LOG.info("successfully got food court image; foodCourtId={}", foodCourtId);
		return Response.status(Response.Status.OK).entity(imageBytes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Get the Food Court of the currently logged-in Food Court Worker Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Food Court of the currently logged-in Food Court Worker Account",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getFoodCourt() throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("get food court request for loginNr={{}}", loginNr);
        FoodCourtResponse data;
        try {
			data = foodCourtService.get(loginNr);
        } catch (ServiceException e) {
			LOG.error("could not get food court for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
        }

		LOG.info("successfully got food court for loginNr={{}}; foodCourtId={{}}", loginNr, data.id());
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Update the Food Court of the currently logged-in Food Court Worker Account")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Updated Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response update(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple request) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if (request.displayName() == null || request.displayName().isBlank()) {
			LOG.error("displayName is null or blank");
			throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		LOG.info("update food court request for loginNr={{}}", loginNr);
        FoodCourtResponse data;
        try {
			data = foodCourtService.update(loginNr, request);
        } catch (ServiceException e) {
			LOG.error("could not update food court for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
        }

		LOG.info("successfully updated food court for loginNr={{}}; foodCourtId={{}}", loginNr, data.id());
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Create a Food Court for the currently logged-in Food Court Worker Account")
	@APIResponses({
			@APIResponse(
					responseCode = "201",
					description = "Created Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "409",
					description = "Already exists / conflict",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")

	})
	public Response create(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple request) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if ( request.displayName() == null ||  request.displayName().isBlank()) {
			LOG.error("displayName is null or blank");
			throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		LOG.info("create food court request for loginNr={{}}", loginNr);
		FoodCourtResponse data;
		try {
			data = foodCourtService.create(loginNr, request);
		} catch (ServiceException e) {
			LOG.error("could not create food court for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
		}

		LOG.info("successfully created food court for loginNr={{}}; foodCourtId={{}}", loginNr, data.id());
		return Response.status(Response.Status.CREATED).entity(data).build();
	}

	@POST
	@Path("image")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Upload/replace the Food Court Image (PNG) for the logged-in Food Court Worker Account")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Image uploaded"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "500",
					description = "Server Error",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "415", description = "Unsupported Media Type"),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addImage(@RestForm("file") @PartType("image/png") InputStream file) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if (file == null) {
			LOG.error("file is null");
			throw new ApiException("The file is null.", Response.Status.BAD_REQUEST);
		}
		LOG.info("add image request for loginNr={{}}", loginNr);
		try (
				PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file))
		) {
			foodCourtService.addImage(loginNr, inputData);
		} catch (ServiceException e) {
			LOG.error("could not add food court image for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e);
		} catch (IOException e) {
			LOG.error("could not add food court image for loginNr={{}}; Exception: ", loginNr, e);
			throw new ApiException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }

		LOG.info("successfully added food court image for loginNr={{}}", loginNr);
		return Response.status(Response.Status.OK).entity(null).build();
	}

	@POST
	@Path("by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Create a Food Court")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Created Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "409",
					description = "Already exists / conflict",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response createById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest request) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		if (id == null) {
			LOG.error("id is null");
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}
		if (request.loginNr() == null || request.loginNr().isBlank()) {
			LOG.error("loginNr is null");
			throw new ApiException("", Response.Status.BAD_REQUEST);
		}
		if (request.displayName() == null || request.displayName().isBlank()) {
			LOG.error("displayName is null");
			throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		LOG.info("create food court by id request for loginNr={{}} and foodCourtId={{}}", loginNr, id);

		FoodCourtResponse data;
		try {
			data = foodCourtService.create(id, request);
		} catch (ServiceException e) {
			LOG.error("could not create food court for loginNr={{}} ; foodCourtId={{}}; Exception: ", loginNr, id, e);
			throw new ApiException(e);
		}

		LOG.info("successfully created food court for loginNr={{}} ; foodCourtId={{}}", loginNr, id);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Update a Food Court by ID")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Updated Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Food Court not found",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response updateById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest request) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("update food court by id request for loginNr={{}} and foodCourtId={{}}", loginNr, id);
		if (id == null) {
			LOG.error("foodCourtId is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}
		if (request.loginNr() == null || request.loginNr().isBlank()) {
			LOG.error("loginNr is null");
			throw new ApiException("The loginNr must not be null.", Response.Status.BAD_REQUEST);
		}
		if (request.displayName() == null || request.displayName().isBlank()) {
			LOG.error("displayName is null");
			throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
		}

		FoodCourtResponse data;
		try {
			data = foodCourtService.update(id, request);
		} catch (ServiceException e) {
			LOG.error("could not update food court for loginNr={{}} ; foodCourtId={{}}; Exception: ", loginNr, id, e);
			throw new ApiException(e);
		}
		LOG.info("successfully updated food court for loginNr={{}} ; foodCourtId={{}}", loginNr, id);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@DELETE
	@Path("by_id/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Delete a Food Court by ID")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Deleted"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "404",
					description = "Food Court not found",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response deleteById(@PathParam("id") UUID id) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("delete food court by id request for loginNr={{}} and foodCourtId={{}}", loginNr, id);
		if (id == null) {
			LOG.error("id is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}

		try {
			foodCourtService.delete(id);
		} catch (ServiceException e) {
			LOG.error("could not delete food court for loginNr={{}} ; foodCourtId={{}}; Exception: ", loginNr, id, e);
			throw new ApiException(e);
		}
		LOG.info("successfully deleted food court for loginNr={{}}; foodCourtId={}", loginNr, id);
		return Response.status(Response.Status.OK).entity("Deleted Product {" + id + "}").build();
	}

	@POST
	@Path("image/by_id/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Upload/replace the Food Court Image (PNG) for the Food Court based on its ID")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Image uploaded"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid Request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "500",
					description = "Server Error",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "415", description = "Unsupported Media Type"),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addImageById(@PathParam("id") UUID id, @RestForm("file") @PartType("image/png") InputStream file) throws ApiException {
		String loginNr;
		try {
			loginNr = validatorService.validateAndGetLoginNr(webToken);
		} catch (ApiException e) {
			LOG.error("invalid authentication; Exception: ", e);
			throw e;
		}
		LOG.info("add food court image by id request for loginNr={{}} and foodCourtId={{}}", loginNr, id);
		try (
				PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file))
		) {
			foodCourtService.addImage(id, inputData);
		} catch (ServiceException e) {
			LOG.error("could not add food court image for loginNr={{}}; foodCourtId={{}}; Exception: ", loginNr, id, e);
			throw new ApiException(e);
		} catch (IOException e) {
			LOG.error("could not add food court image for loginNr={{}}; foodCourtId={{}}; Exception: ", loginNr, id, e);
			throw new ApiException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
		LOG.info("successfully added food court image for loginNr={{}}; foodCourtId={{}}", loginNr, id);
		return Response.status(Response.Status.OK).entity(null).build();
	}
}
