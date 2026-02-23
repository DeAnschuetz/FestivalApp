package com.ffb.app.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.ffb.model.api.response.food_court.FoodCourtRequest;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import com.ffb.app.service.api.api.food.court.FoodCourtService;

import com.ffb.model.api.response.food_court.FoodCourtRequestSimple;
import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
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
	JsonWebToken jwt;
	private final FoodCourtService foodCourtService;

	@Inject
	public FoodCourtApi(FoodCourtService foodCourtService) {
		this.foodCourtService = foodCourtService;
	}

	@GET
	@Path("list_all")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	public Response listAll() {
		List<FoodCourt> data = foodCourtService.listAll();
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	public Response getById(@PathParam("id") UUID id) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}

		FoodCourt data;
        try {
			data = foodCourtService.getById(id);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
		return Response.status(Response.Status.OK).entity(data).build();
    }

	@GET
	@Path("by_id/{id}/with-relations")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GUEST")
	public Response getWithRelationsById(@PathParam("id") UUID id, @QueryParam("waitingTime") @DefaultValue("true") boolean waitingTime, @DefaultValue("false") boolean foodOrders) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
		}

        FoodCourt data;
        try {
            data = foodCourtService.getWithRelations(id, waitingTime, foodOrders);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Produces("image/png")
	@Path("image/by_food_court_id/{foodCourtId}")
	@RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
	public Response getImageByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
		byte[] imageBytes;
		try {
			imageBytes = foodCourtService.getImageByFoodCourtId(foodCourtId);
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
	public Response getFoodCourt() throws ApiException {
		String loginNr = jwt.getName();
        FoodCourt data;
        try {
            data = foodCourtService.getByLoginNr(loginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response update(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) throws ApiException {
		String loginNr = jwt.getName();
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.", Response.Status.BAD_REQUEST);
		}

        FoodCourt data;
        try {
            data = foodCourtService.updateByLoginNr(loginNr, name);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response create(@PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) throws ApiException {
		String loginNr = jwt.getName();
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.", Response.Status.BAD_REQUEST);
		}
        FoodCourt data;
        try {
            data = foodCourtService.create(loginNr, name);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("image")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response addImage(
			@RestForm("file") @PartType("image/png") InputStream file
	) throws ApiException {
		LOG.info("Received request to add image");
		String loginNr = jwt.getName();

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
	@Path("admin/by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
	public Response createById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) throws ApiException {
		if (id == null) {
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
		FoodCourt data;
		try {
			data = foodCourtService.create(loginNr, name);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("admin/by_id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed("ADMIN")
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

		FoodCourt data = null;
		try {
			data = foodCourtService.updateById(id, loginNr, name);
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@DELETE
	@Path("admin/by_id/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	@RolesAllowed("ADMIN")
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
}
