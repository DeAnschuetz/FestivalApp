package com.ffb.app.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.request.food.court.FoodCourtRequest;
import com.ffb.model.api.response.food_court.FoodCourtResponse;
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
import org.jboss.logging.Logger;
import com.ffb.app.service.api.api.food.court.FoodCourtService;
import com.ffb.model.api.request.food.court.FoodCourtRequestSimple;
import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

@ApplicationScoped
@Path("food_court")
public class FoodCourtApi {

	private static final Logger LOG = Logger.getLogger(FoodCourtApi.class);

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
		List<FoodCourtResponse> data = foodCourtService.listAll();
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("by_id/{id}")
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
	public Response getById(@PathParam("id") UUID id) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}

		FoodCourtResponse data;
        try {
			data = foodCourtService.get(id);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
		return Response.status(Response.Status.OK).entity(data).build();
    }

	@GET
	@Path("by_id/{id}/with-relations")
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
	public Response getWithRelationsById(@PathParam("id") UUID id, @QueryParam("waitingTime") @DefaultValue("true") boolean waitingTime, @DefaultValue("false") boolean foodOrders) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}

        FoodCourtResponse data;
        try {
			data = foodCourtService.get(id, waitingTime, foodOrders);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Produces("image/png")
	@Path("image/by_food_court_id/{foodCourtId}")
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
	public Response getImageByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
		byte[] imageBytes;
		try {
			imageBytes = foodCourtService.getImage(foodCourtId);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}

		if (imageBytes == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
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
		String loginNr = webToken.getName();
        FoodCourtResponse data;
        try {
			data = foodCourtService.get(loginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
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
	public Response update(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) throws ApiException {
		String loginNr = webToken.getName();
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.", Response.Status.BAD_REQUEST);
		}

        FoodCourtResponse data;
        try {
			data = foodCourtService.update(loginNr, name);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
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
	public Response create(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) throws ApiException {
		String loginNr = webToken.getName();
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		FoodCourtResponse data;
		try {
			data = foodCourtService.create(loginNr, name);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
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
		LOG.info("Received request to add image");
		String loginNr = webToken.getName();

		if (file == null) {
			throw new ApiException("The file is null.", Response.Status.BAD_REQUEST);
		}

		try (
				PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file))
		) {
			foodCourtService.addImage(loginNr, inputData);

		} catch (ServiceException e) {
			throw new ApiException(e);
		} catch (IOException e) {
            throw new ApiException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }

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
	public Response createById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) throws ApiException {
		if (id == null) {
			// TODO
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}
		String loginNr = req.loginNr();
		if (loginNr == null || loginNr.isBlank()) {
			// TODO
			throw new ApiException("", Response.Status.BAD_REQUEST);
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.", Response.Status.BAD_REQUEST);
		}
		FoodCourtResponse data;
		try {
			data = foodCourtService.create(id, loginNr, name);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
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
	public Response updateById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}
		String loginNr = req.loginNr();
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The account id must not be null.", Response.Status.BAD_REQUEST);
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.", Response.Status.BAD_REQUEST);
		}

		FoodCourtResponse data;
		try {
			data = foodCourtService.update(id, loginNr, name);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
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
		if (id == null) {
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}

		try {
			foodCourtService.delete(id);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(null).build();
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
		LOG.info("Received request to add image");
		try (
				PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file))
		) {
			foodCourtService.addImage(id, inputData);
		} catch (ServiceException e) {
			throw new ApiException(e);
		} catch (IOException e) {
			throw new ApiException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}

		return Response.status(Response.Status.OK).entity(null).build();
	}
}
