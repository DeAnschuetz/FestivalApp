package com.ffb.app.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ffb.model.api.response.foodcourt.Foodcourt;
import com.ffb.model.api.response.foodcourt.ProductFull;
import com.ffb.model.api.response.foodcourt.ProductSimple;
import com.ffb.model.api.response.foodcourt.SubProductFull;
import com.ffb.model.api.response.response.Response;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/foodcourt")
public class FoodcourtApi {

	
    @GET
	public Response getAll() {
    	
    	List<ProductSimple> products = new ArrayList<ProductSimple>();
    	products.add(new ProductSimple(UUID.randomUUID(), "Burger", (byte)0, 1));
    	products.add(new ProductSimple(UUID.randomUUID(), "Pommes", (byte)0, 1));
    	products.add(new ProductSimple(UUID.randomUUID(), "Cola", (byte)0, 1));
    	products.add(new ProductSimple(UUID.randomUUID(), "Burger Menü", (byte)0, 1));
    	
		List<Foodcourt> data = new ArrayList<Foodcourt>();
		data.add(new Foodcourt(UUID.randomUUID(), "Burger Place", (byte)0, 0, products));
		
		Response response = new Response(200, null, data);
		return response;
	}
	
	@Path("{foodcourtId}")
    @GET
	public Response getFoodcourtDataByID(UUID foodcourtId) {
    	
    	List<SubProductFull> subProducts = new ArrayList<SubProductFull>();
    	subProducts.add(new SubProductFull(UUID.randomUUID(), "Burger", (byte)0, 1));
    	subProducts.add(new SubProductFull(UUID.randomUUID(), "Pommes", (byte)0, 1));
    	subProducts.add(new SubProductFull(UUID.randomUUID(), "Cola", (byte)0, 1));
		
    	List<ProductFull> data = new ArrayList<ProductFull>();
    	data.add(new ProductFull(UUID.randomUUID(), "Burger", (byte)0, 1, null));
    	data.add(new ProductFull(UUID.randomUUID(), "Pommes", (byte)0, 1, null));
    	data.add(new ProductFull(UUID.randomUUID(), "Cola", (byte)0, 1, null));
    	data.add(new ProductFull(UUID.randomUUID(), "Burger Menü", (byte)0, 1, subProducts));
    	
    	
		Response response = new Response(200, null, data);
		return response;
	}
}
