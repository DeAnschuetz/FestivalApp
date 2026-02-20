package com.ffb.app.api;

import java.util.List;
import java.util.UUID;
import com.ffb.app.service.api.foodcourt.FoodcourtService;
import com.ffb.model.api.response.foodcourt.FoodcourtRequest;
import com.ffb.model.api.response.response.Response;
import com.ffb.model.db.objects.foodcourt.Foodcourt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.PartType;

@ApplicationScoped
@Path("/foodcourt")
public class FoodcourtApi {

	FoodcourtService foodcourtService;

	@Inject
	public FoodcourtApi(FoodcourtService foodcourtService) {
		this.foodcourtService = foodcourtService;
	}

	@GET
	@Path("/{accountId}")
	public List<Foodcourt> listAll(@PathParam("accountId") UUID accountId) {
		List<Foodcourt> list = (accountId == null)
				? foodcourtService.listAll()
				: foodcourtService.listByAccountId(accountId);

		return list;
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") UUID id) {
		return null;
	}

	@POST
	public Response create(FoodcourtRequest req) {
		Foodcourt created = foodcourtService.create(req);
		return null;
	}

	@PUT
	@Path("/{id}")
	public Response update(@PathParam("id") UUID id, @PartType(MediaType.APPLICATION_JSON) FoodcourtRequest req) {
		return null;
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") UUID id) {
		foodcourtService.delete(id);
		return null;
	}

	@GET
	@Path("/{id}/with-relations")
	public Response getWithRelations(@PathParam("id") UUID id, @QueryParam("waitingTime") @DefaultValue("true") boolean waitingTime, @QueryParam("foodOrders") @DefaultValue("false") boolean foodOrders) {
		Foodcourt fc = foodcourtService.getWithRelations(id, waitingTime, foodOrders);
		return null;
	}
}
