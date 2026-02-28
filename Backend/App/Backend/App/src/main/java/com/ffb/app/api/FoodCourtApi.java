package com.ffb.app.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;

import com.ffb.model.api.request.food.court.FoodCourtWithRelationsRequest;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.request.food.court.FoodCourtRequest;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import com.ffb.app.service.api.food.court.FoodCourtService;
import com.ffb.model.api.request.food.court.FoodCourtRequestSimple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Path("food_court")
public class FoodCourtApi {

	// TODO Logging
	private final Logger LOG = LoggerFactory.getLogger(FoodCourtApi.class);

	@Inject
	JsonWebToken webToken;
	private final FoodCourtService foodCourtService;

	@Inject
	public FoodCourtApi(FoodCourtService foodCourtService) {
		this.foodCourtService = foodCourtService;
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
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response listAll() {
		LOG.info("list all foodCourts");
		List<FoodCourtResponse> data = foodCourtService.listAll();
		LOG.info("found " + data.size() + " foodCourts");
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("by_id/{foodCourtId}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({"GUEST", "ADMIN"})
	@Operation(summary = "Get a FoodCourt By its ID")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Food Court with the given ID",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CreditResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
		LOG.info("geting {" + foodCourtId + "}");
		if (foodCourtId == null) {
			LOG.error("foodCourtId is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}

		FoodCourtResponse data;
        try {
			data = foodCourtService.get(foodCourtId);
        } catch (ServiceException e) {
			LOG.error("could not get foodCourt {" + foodCourtId + "}");
            throw new ApiException(e);
        }

		LOG.info("got foodCourt {" + foodCourtId + "}");
		return Response.status(Response.Status.OK).entity(data).build();
    }

	@GET
	@Path("by_id/{foodCourtId}/with-relations")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	@Operation(summary = "Get a Food Court by ID with optional relations")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Food Court with relations based on query parameters",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
	public Response getWithRelationsById(FoodCourtWithRelationsRequest request) throws ApiException {
		LOG.info("getting with relations {{}}", request.foodCourtId());
		if (request.foodCourtId() == null) {
			LOG.error("foodCourtId is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}

        FoodCourtResponse data;
        try {
			data = foodCourtService.get(request);
        } catch (ServiceException e) {
			LOG.error("could not get foodCourt {{}}; Exception:", request.foodCourtId(), e);
            throw new ApiException(e);
        }

		LOG.info("got with relations for {{}}", request.foodCourtId());
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Produces("image/png")
	@Path("image/{foodCourtId}")
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	@Operation(summary = "Get the Food Court image by Food Court ID (PNG)")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "PNG image bytes",
					content = @Content(mediaType = "image/png", schema = @Schema(type = SchemaType.STRING, format = "binary"))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
		LOG.info("get image for {" + foodCourtId + "}");
		if(foodCourtId == null) {
			LOG.error("foodCourtId is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}
		byte[] imageBytes;
		try {
			imageBytes = foodCourtService.getImage(foodCourtId);
		} catch (ServiceException e) {
			LOG.error("could not get iamge for {" + foodCourtId + "}", e);
			throw new ApiException(e);
		}

		if (imageBytes == null) {
			LOG.error("could not get iamge for {" + foodCourtId + "}");
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		LOG.info("got image for {" + foodCourtId + "}");
		return Response.status(Response.Status.OK).entity(imageBytes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Get the Food Court of the currently logged-in worker")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "The Food Court of the logged-in worker",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response getFoodCourt() throws ApiException {
		LOG.info("get foodCourt for " + webToken.getName());
		String loginNr = webToken.getName();
        FoodCourtResponse data;
        try {
			data = foodCourtService.get(loginNr);
        } catch (ServiceException e) {
			LOG.error("could not get foodCourt for " + loginNr, e);
            throw new ApiException(e);
        }

		LOG.info("got foodCourt {" + data.id() + "}");
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Update the Food Court of the currently logged-in worker")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Updated Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response update(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple request) throws ApiException {
		LOG.info("updating foodCourt for " + webToken.getName());
		String loginNr = webToken.getName();
		if (request.displayName() == null || request.displayName().isBlank()) {
			LOG.error("displayName is null or blank");
			throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
		}

        FoodCourtResponse data;
        try {
			data = foodCourtService.update(loginNr, request);
        } catch (ServiceException e) {
			LOG.error("could not update foodCourt for " + loginNr, e);
            throw new ApiException(e);
        }

		LOG.info("updated foodCourtfor " + loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Create a Food Court for the currently logged-in worker")
	@APIResponses({
			@APIResponse(
					responseCode = "201",
					description = "Created Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
		LOG.info("creating foodCourt for " + webToken.getName());
		String loginNr = webToken.getName();
		if ( request.displayName() == null ||  request.displayName().isBlank()) {
			LOG.error("displayName is null or blank");
			throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		FoodCourtResponse data;
		try {
			data = foodCourtService.create(loginNr, request);
		} catch (ServiceException e) {
			LOG.error("could not crea foodCourt for " + loginNr);
			throw new ApiException(e);
		}

		LOG.info("foodCourt created for " + loginNr);
		return Response.status(Response.Status.CREATED).entity(data).build();
	}

	@POST
	@Path("image")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("FOOD_COURT_WORKER")
	@Operation(summary = "Upload/replace the Food Court image (PNG) for the logged-in worker")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Image uploaded"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "500",
					description = "Server error",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "415", description = "Unsupported Media Type"),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addImage(@RestForm("file") @PartType("image/png") InputStream file) throws ApiException {
		LOG.info("received request to add image for " + webToken.getName());
		String loginNr = webToken.getName();
		if (file == null) {
			LOG.error("file is null");
			throw new ApiException("The file is null.", Response.Status.BAD_REQUEST);
		}

		try (
				PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file))
		) {
			foodCourtService.addImage(loginNr, inputData);
		} catch (ServiceException e) {
			LOG.error("could not add image", e);
			throw new ApiException(e);
		} catch (IOException e) {
			LOG.error("could not add image", e);
            throw new ApiException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }

		LOG.info("added image for " + loginNr);
        return Response.status(Response.Status.OK).entity(null).build();
	}

	@POST
	@Path("by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Admin: create a Food Court (path ID currently not used unless service supports it)")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Created Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
		LOG.info("creating {" + id + "}");
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

		FoodCourtResponse data;
		try {
			data = foodCourtService.create(id, request);
		} catch (ServiceException e) {
			LOG.error("could not create foodCourt {" + id + "}");
			throw new ApiException(e);
		}

		LOG.info("created {" + id + "}");
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Admin: update a Food Court by ID")
	@APIResponses({
			@APIResponse(
					responseCode = "200",
					description = "Updated Food Court",
					content = @Content(schema = @Schema(implementation = FoodCourtResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
		LOG.info("updating foodCourt {" + id + "}");
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
			LOG.error("could not update foodCourt {" + id + "}", e);
			throw new ApiException(e);
		}
		LOG.info("updated foodCourt {" + id + "}");
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@DELETE
	@Path("by_id/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Admin: delete a Food Court by ID")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Deleted"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
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
		LOG.info("deleting foodCourt {" + id + "}");
		if (id == null) {
			LOG.error("id is null");
			throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
		}

		try {
			foodCourtService.delete(id);
		} catch (ServiceException e) {
			LOG.error("could not delete {" + id + "}");
			throw new ApiException(e);
		}
		LOG.info("deleted {" + id + "}");
		return Response.status(Response.Status.OK).entity("Deleted Product {" + id + "}").build();
	}

	@POST
	@Path("image/by_id/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("ADMIN")
	@Operation(summary = "Upload/replace the Food Court image (PNG) for the logged-in worker")
	@APIResponses({
			@APIResponse(responseCode = "200", description = "Image uploaded"),
			@APIResponse(
					responseCode = "400",
					description = "Invalid request",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(
					responseCode = "500",
					description = "Server error",
					content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT))
			),
			@APIResponse(responseCode = "415", description = "Unsupported Media Type"),
			@APIResponse(responseCode = "401", description = "Not Authorized"),
			@APIResponse(responseCode = "403", description = "Not Allowed")
	})
	public Response addImageById(@PathParam("id") UUID id, @RestForm("file") @PartType("image/png") InputStream file) throws ApiException {
		LOG.info("received request to add image for foodCourt {" + id + "}");
		try (
				PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file))
		) {
			foodCourtService.addImage(id, inputData);
		} catch (ServiceException e) {
			LOG.error("could not add image", e);
			throw new ApiException(e);
		} catch (IOException e) {
			LOG.error("could not add image", e);
			throw new ApiException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
		LOG.info("added image for foodCourt {" + id + "}");
		return Response.status(Response.Status.OK).entity(null).build();
	}
}
