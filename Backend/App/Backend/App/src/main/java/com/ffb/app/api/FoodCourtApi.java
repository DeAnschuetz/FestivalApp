package com.ffb.app.api;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import com.ffb.app.service.api.food.court.FoodCourtService;
import com.ffb.model.api.response.food_court.FoodCourtRequestSimple;
import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;

@ApplicationScoped
@Path("/food_court")
public class FoodCourtApi {

	FoodCourtService foodCourtService;

	@Inject
	public FoodCourtApi(FoodCourtService foodCourtService) {
		this.foodCourtService = foodCourtService;
	}

	@GET
	public Response listAll() {
		List<FoodCourt> data = foodCourtService.listAll();
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("/{accountId}")
	public Response listByAccountId(@PathParam("accountId") UUID accountId) {
		if (accountId == null) {
			throw new WebApplicationException("The account id must not be null.");
		}

		List<FoodCourt> data = foodCourtService.listByAccountId(accountId);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@GET
	@Path("/{id}")
	public Response getByAccountId(@PathParam("id") UUID id) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}

		return null;
	}

	@POST
	public Response create(FoodCourtRequestSimple req) {
		UUID accountId = req.accountId();
		if (accountId == null) {
			throw new WebApplicationException("The account id must not be null.");
		}
		String name = req.name();
		if (name == null | name.isBlank()) {
			throw new WebApplicationException("The name must not be null or blank.");
		}
		URI imageUri = req.imageUri();
		if (imageUri == null) {
			throw new WebApplicationException("The image URI must not be null.");
		}

		FoodCourt data = foodCourtService.create(accountId, name, imageUri);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@PUT
	@Path("/{id}")
	public Response updateById(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodCourtRequestSimple req) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}
		UUID accountId = req.accountId();
		if (accountId == null) {
			throw new WebApplicationException("The account id must not be null.");
		}
		String name = req.name();
		if (name == null | name.isBlank()) {
			throw new WebApplicationException("The name must not be null or blank.");
		}
		URI imageUri = req.imageUri();
		if (imageUri == null) {
			throw new WebApplicationException("The image URI must not be null.");
		}

		FoodCourt data = foodCourtService.update(id, accountId, name, imageUri);
		return Response.status(Response.Status.OK).entity(data).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteById(@PathParam("id") UUID id) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}

		foodCourtService.delete(id);
		return Response.status(Response.Status.OK).entity(null).build();
	}

	@GET
	@Path("/{id}/with-relations")
	public Response getWithRelationsById(@PathParam("id") UUID id, @QueryParam("waitingTime") @DefaultValue("true") boolean waitingTime, @QueryParam("foodOrders") @DefaultValue("false") boolean foodOrders) {
		if (id == null) {
			throw new WebApplicationException("The food court id must not be null.");
		}

		FoodCourt data = foodCourtService.getWithRelations(id, waitingTime, foodOrders);
		return Response.status(Response.Status.OK).entity(data).build();
	}
}
