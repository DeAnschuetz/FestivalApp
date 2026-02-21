package com.ffb.app.api;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.ffb.model.api.response.food_court.FoodCourtRequest;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
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
@Path("/food_court")
public class FoodCourtApi {

	private static final Logger LOG = Logger.getLogger(FoodCourtApi.class);

	private static final String ALLOWED_FILE = "image/png";

	private final FoodCourtService foodCourtService;

	@Inject
	public FoodCourtApi(FoodCourtService foodCourtService) {
		this.foodCourtService = foodCourtService;
	}

	@GET
	@Path("/list/all")
	@RolesAllowed("GUEST")
	public Response listAll() {
		List<FoodCourt> data = foodCourtService.listAll();
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("/list/by_id/{id}")
	@RolesAllowed("GUEST")
	public Response getById(@PathParam("id") UUID id) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.");
		}

		return null;
	}

	@GET
	@Path("/get/by_id/{id}/with-relations")
	@RolesAllowed("GUEST")
	public Response getWithRelationsById(@PathParam("id") UUID id, @QueryParam("waitingTime") @DefaultValue("true") boolean waitingTime, @QueryParam("foodOrders") @DefaultValue("false") boolean foodOrders) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.");
		}

        FoodCourt data = null;
        try {
            data = foodCourtService.getWithRelations(id, waitingTime, foodOrders);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/by_id/{id}")
	@RolesAllowed("ADMIN")
	public Response createById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.");
		}
		String loginNr = req.loginNr();
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.");
		}
        FoodCourt data = null;
        try {
            data = foodCourtService.create(loginNr, name);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("/by_id/{id}")
	@RolesAllowed("ADMIN")
	public Response updateById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.");
		}
		String loginNr = req.loginNr();
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The account id must not be null.");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.");
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
	@Path("/by_id/{id}")
	@RolesAllowed("ADMIN")
	public Response deleteById(@PathParam("id") UUID id) throws ApiException {
		if (id == null) {
			throw new ApiException("The food court id must not be null.");
		}

        try {
            foodCourtService.delete(id);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(null).build();
	}

	@GET
	@Path("/get/by_login_nr/{loginNr}")
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response getByLoginNr(@PathParam("loginNr") String loginNr) throws ApiException {
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The loginNr must not be null.");
		}

        FoodCourt data = null;
        try {
            data = foodCourtService.getByLoginNr(loginNr);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("/by_login_nr/{loginNr}")
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response updateByLogiNr(@PathParam("loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) throws ApiException {
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The food LoginNr must not be null.");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.");
		}

        FoodCourt data = null;
        try {
            data = foodCourtService.updateByLoginNr(loginNr, name);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/by_login_nr/{loginNr}")
	@RolesAllowed("ADMIN")
	public Response createByLoginNr(@PathParam("loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) throws ApiException {
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The food loginNr must not be null.");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new ApiException("The name must not be null or blank.");
		}
        FoodCourt data = null;
        try {
            data = foodCourtService.create(loginNr, name);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/image/by_loin_nr/{loginNr}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RolesAllowed("FOOD_COURT_WORKER")
	public Response addImage(
			@PathParam("loginNr") String loginNr,
			@RestForm("file") @PartType("image/png") InputStream file
	) throws ApiException {
		LOG.info("Received request to add image");
		if (loginNr == null || loginNr.isBlank()) {
			throw new ApiException("The login number must not be null or blank.");
		}
		if (file == null) {
			throw new ApiException("The file is null.");
		}

		PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file));
		try {
			URI data = foodCourtService.addImage(loginNr, inputData);
			return Response.status(Response.Status.OK).entity(data).build();
		} catch (ServiceException e) {
			throw new ApiException(e);
		}
	}

	@GET
	@Path("/image/by_loin_nr/{foodCourtId}")
	@Produces("image/png")
	@RolesAllowed("GUEST")
	public Response getImageByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) {
		byte[] imageBytes = foodCourtService.getImageByFoodCourtId(foodCourtId);

		if (imageBytes == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.status(Response.Status.OK).entity(imageBytes).type("image/png").build();
	}
}
