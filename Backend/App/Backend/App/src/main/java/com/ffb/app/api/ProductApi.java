package com.ffb.app.api;

import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
@Path("/products")
public class ProductApi {

    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductRequest req) {
        UUID foodcourtId = req.foodcourtId();
        if (foodcourtId == null) {
            throw new WebApplicationException("");
        }
        double price = req.price();
        if (price == 0) {
            throw new WebApplicationException("");
        }
        String displayName = req.displayName();
        if (displayName == null | displayName.isBlank()) {
            throw new WebApplicationException("");
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null | symbolIdentifier.isBlank()) {
            throw new WebApplicationException("");
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new WebApplicationException("");
        }

        try {
            Product created = productService.createProduct(req);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{productId}")
    public Response createProduct(@PathParam("productId") UUID productId, @PartType(MediaType.APPLICATION_JSON) ProductRequest req) {
        UUID foodcourtId = req.foodcourtId();
        if (foodcourtId == null) {
            throw new WebApplicationException("");
        }
        double price = req.price();
        if (price == 0) {
            throw new WebApplicationException("");
        }
        String displayName = req.displayName();
        if (displayName == null | displayName.isBlank()) {
            throw new WebApplicationException("");
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null | symbolIdentifier.isBlank()) {
            throw new WebApplicationException("");
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new WebApplicationException("");
        }

        try {
            Product created = productService.createProductWithId(productId, req);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (KeyAlreadyExistsException | EntityNotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("id") UUID id) {
        if (id == null) {
            throw new WebApplicationException("");
        }

        try {
            Product data = productService.getProductById(id);
            return Response.status(Response.Status.OK).entity(data).build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProducts() {
        List<Product> data = productService.listProducts();
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Path("/{foodcourtId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProductsByFoodcourtId(@PathParam("foodcourtId") UUID foodcourtId) {
        if (foodcourtId == null) {
            throw new WebApplicationException("");
        }

        List<Product> data = productService.listProductsByFoodcourtId(foodcourtId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") UUID id) {
        if (id == null) {
            throw new WebApplicationException("");
        }

        try {
            productService.deleteProductById(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @POST
    @Path("/assignments/{mainProductId}/{subProductId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAssignment(@PathParam("mainProductId") UUID mainProductId, @PathParam("subProductId") UUID subProductId) {
        if (mainProductId == null) {
            throw new WebApplicationException("");
        }
        if (subProductId == null) {
            throw new WebApplicationException("");
        }

        try {
            UUID linkId = productService.createAssignment(mainProductId, subProductId);
            return Response.status(Response.Status.CREATED).entity(linkId).build();
        } catch (IllegalArgumentException | NoSuchElementException | IllegalStateException | PersistenceException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("/assignments/{mainProductId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAssignmentsForMainProduct(@PathParam("mainProductId") UUID mainProductId) {
        if (mainProductId == null) {
            throw new WebApplicationException("");
        }

        try {
            List<MainSubProductLink> data = productService.listAssignmentsForMainProduct(mainProductId);
            return Response.status(Response.Status.CREATED).entity(data).build();
        } catch ( IllegalArgumentException | IllegalStateException | PersistenceException e) {
            throw new WebApplicationException(e);
        }
    }

    @DELETE
    @Path("/assignments/{id}")
    public Response deleteAssignmentById(@PathParam("id") UUID id) {
        if (id == null) {
            throw new WebApplicationException("");
        }

        try {
            productService.deleteAssignmentById(id);
            return Response.noContent().build();
        } catch (NotFoundException | IllegalArgumentException | TransactionRequiredException e) {
            throw new WebApplicationException(e);
        }
    }

    @DELETE
    @Path("/assignments/{mainProductId}/{subProductId}")
    public Response deleteAssignmentByPair(@PathParam("mainProductId") UUID mainProductId, @PathParam("subProductId") UUID subProductId) {
        if (mainProductId == null) {
            throw new WebApplicationException("");
        }
        if (subProductId == null) {
            throw new WebApplicationException("");
        }

        try {
            productService.deleteAssignmentByPair(mainProductId, subProductId);
            return Response.noContent().build();
        } catch (NotFoundException | PersistenceException | IllegalArgumentException | IllegalStateException e) {
            throw new WebApplicationException(e);
        }
    }
}
