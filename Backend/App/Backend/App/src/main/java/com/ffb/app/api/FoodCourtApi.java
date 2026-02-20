package com.ffb.app.api;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.ffb.model.api.response.food_court.FoodCourtRequest;
import org.jboss.logging.Logger;

import com.ffb.app.service.api.food.court.FoodCourtService;

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
	public Response listAll() {
		List<FoodCourt> data = foodCourtService.listAll();
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("/list/by_id/{id}")
	public Response getById(@PathParam("id") UUID id) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}

		return null;
	}

	@GET
	@Path("/get/by_id/{id}/with-relations")
	public Response getWithRelationsById(@PathParam("id") UUID id, @QueryParam("waitingTime") @DefaultValue("true") boolean waitingTime, @QueryParam("foodOrders") @DefaultValue("false") boolean foodOrders) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}

		FoodCourt data = foodCourtService.getWithRelations(id, waitingTime, foodOrders);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/by_id/{id}")
	public Response createById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}
		String loginNr = req.loginNr();
		if (loginNr == null || loginNr.isBlank()) {
			throw new WebApplicationException("");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new WebApplicationException("The name must not be null or blank.");
		}
		FoodCourt data = foodCourtService.create(loginNr, name);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("/by_id/{id}")
	public Response updateById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequest req) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}
		String loginNr = req.loginNr();
		if (loginNr == null || loginNr.isBlank()) {
			throw new WebApplicationException("The account id must not be null.");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new WebApplicationException("The name must not be null or blank.");
		}

		FoodCourt data = foodCourtService.updateById(id, loginNr, name);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@DELETE
	@Path("/by_id/{id}")
	public Response deleteById(@PathParam("id") UUID id) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}

		foodCourtService.delete(id);
		return Response.status(Response.Status.OK).entity(null).build();
	}

	@GET
	@Path("/get/by_login_nr/{loginNr}")
	public Response getByLoginNr(@PathParam("loginNr") String loginNr) {
		if (loginNr == null || loginNr.isBlank()) {
			throw new WebApplicationException("");
		}

		FoodCourt data = foodCourtService.getByLoginNr(loginNr);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("/by_login_nr/{loginNr}")
	public Response updateByLogiNr(@PathParam("loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) {
		if (loginNr == null || loginNr.isBlank()) {
			throw new WebApplicationException("The food court id must not be null.");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new WebApplicationException("The name must not be null or blank.");
		}

		FoodCourt data = foodCourtService.updateByLoginNr(loginNr, name);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/by_login_nr/{loginNr}")
	public Response createByLoginNr(@PathParam("loginNr") String loginNr, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) {
		if (loginNr == null || loginNr.isBlank()) {
			throw new WebApplicationException("");
		}
		String name = req.name();
		if (name == null || name.isBlank()) {
			throw new WebApplicationException("The name must not be null or blank.");
		}
		FoodCourt data = foodCourtService.create(loginNr, name);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@POST
	@Path("/image/by_loin_nr/{loginNr}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addImage(
			@PathParam("loginNr") String loginNr,
			@RestForm("file") @PartType("image/png") InputStream file
	) {
		LOG.info("Received request to add image");
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("LoginNr is null or blank.");
			return Response.status(Response.Status.BAD_REQUEST).entity("1").build();
		}
		if (file == null) {
			LOG.error("File is null or blank.");
			return Response.status(Response.Status.BAD_REQUEST).entity("2").build();
		}

		PushbackInputStream inputData = new PushbackInputStream(new BufferedInputStream(file));
		try {
			URI data = foodCourtService.addImage(loginNr, inputData);
			return Response.status(Response.Status.OK).entity(data).build();
		} catch (EntityNotFoundException e) {
			LOG.error(e.getMessage());
			return Response.status(Response.Status.BAD_REQUEST).entity("6").build();
		}
	}
}
