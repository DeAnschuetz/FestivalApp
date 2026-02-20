package com.ffb.app.api;

import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.db.objects.product.MainSubProductLink;
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
@Path("/products/assignments")
public class ProductAssignmentApi {

    private final ProductService productService;

    @Inject
    public ProductAssignmentApi(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Path("/by_main_sub_id/{mainProductId}/{subProductId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAssignmentForIds(@PathParam("mainProductId") UUID mainProductId, @PathParam("subProductId") UUID subProductId) {
        if (mainProductId == null) {
            throw new WebApplicationException("The main product id must not be null.");
        }
        if (subProductId == null) {
            throw new WebApplicationException("The sub product id must not be null.");
        }

        try {
            UUID linkId = productService.createAssignment(mainProductId, subProductId);
            return Response.status(Response.Status.CREATED).entity(linkId).build();
        } catch (IllegalArgumentException | NoSuchElementException | IllegalStateException | PersistenceException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("/list/by_main_id/{mainProductId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAssignmentsByMainProductId(@PathParam("mainProductId") UUID mainProductId) {
        if (mainProductId == null) {
            throw new WebApplicationException("The main product id must not be null.");
        }

        try {
            List<MainSubProductLink> data = productService.listAssignmentsForMainProduct(mainProductId);
            return Response.status(Response.Status.CREATED).entity(data).build();
        } catch ( IllegalArgumentException | IllegalStateException | PersistenceException e) {
            throw new WebApplicationException(e);
        }
    }

    @DELETE
    @Path("/by_id/{id}")
    public Response deleteAssignmentById(@PathParam("id") UUID id) {
        if (id == null) {
            throw new WebApplicationException("The assignment id must not be null.");
        }

        try {
            productService.deleteAssignmentById(id);
            return Response.status(Response.Status.OK).entity(null).build();
        } catch (NotFoundException | IllegalArgumentException | TransactionRequiredException e) {
            throw new WebApplicationException(e);
        }
    }

    @DELETE
    @Path("/by_main_sub_id/{mainProductId}/{subProductId}")
    public Response deleteAssignmentByMainSubProductIds(@PathParam("mainProductId") UUID mainProductId, @PathParam("subProductId") UUID subProductId) {
        if (mainProductId == null) {
            throw new WebApplicationException("The main product id must not be null.");
        }
        if (subProductId == null) {
            throw new WebApplicationException("The sub product id must not be null.");
        }

        try {
            productService.deleteAssignmentByPair(mainProductId, subProductId);
            return Response.status(Response.Status.OK).entity(null).build();
        } catch (NotFoundException | PersistenceException | IllegalArgumentException | IllegalStateException e) {
            throw new WebApplicationException(e);
        }
    }
}
