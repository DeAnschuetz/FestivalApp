package com.ffb.app.api;

import com.ffb.app.service.api.api.product.ProductService;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
@Path("products/assignments")
public class ProductAssignmentApi {

    private final ProductService productService;

    @Inject
    public ProductAssignmentApi(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Path("by_main_sub_id/{mainProductId}/{subProductId}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response createAssignmentForIds(@PathParam("mainProductId") UUID mainProductId, @PathParam("subProductId") UUID subProductId) throws ApiException {
        if (mainProductId == null) {
            throw new WebApplicationException("The main product id must not be null.");
        }
        if (subProductId == null) {
            throw new WebApplicationException("The sub product id must not be null.");
        }

        boolean result;
        try {
            result = productService.createAssignment(mainProductId, subProductId);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @GET
    @Path("list/by_main_id/{mainProductId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response listAssignmentsByMainProductId(@PathParam("mainProductId") UUID mainProductId) throws ApiException {
        if (mainProductId == null) {
            throw new ApiException("The main product id must not be null.");
        }

        List<MainSubProductLink> data = productService.listAssignmentsForMainProduct(mainProductId);
        return Response.status(Response.Status.CREATED).entity(data).build();

    }

    @DELETE
    @Path("by_id/{id}")
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response deleteAssignmentById(@PathParam("id") UUID id) throws ApiException {
        if (id == null) {
            throw new ApiException("The assignment id must not be null.");
        }

        productService.deleteAssignmentById(id);
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @DELETE
    @Path("by_main_sub_id/{mainProductId}/{subProductId}")
    @RolesAllowed("ADMIN")
    public Response deleteAssignmentByMainSubProductIds(@PathParam("mainProductId") UUID mainProductId, @PathParam("subProductId") UUID subProductId) throws ApiException {
        if (mainProductId == null) {
            throw new ApiException("The main product id must not be null.");
        }
        if (subProductId == null) {
            throw new ApiException("The sub product id must not be null.");
        }

        try {
            productService.deleteAssignmentByPair(mainProductId, subProductId);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(null).build();
    }
}
