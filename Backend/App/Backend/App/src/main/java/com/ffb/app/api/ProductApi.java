package com.ffb.app.api;

import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.product.ProductRequestSimple;
import com.ffb.model.db.objects.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.PartType;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("/products")
public class ProductApi {

    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/all")
    public Response listAll() {
        List<Product> data = productService.listProducts();
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/by_login_nr/{loginNr}")
    public Response createProductByLoginNr(@PathParam("loginNr") String loginNr, ProductRequestSimple req) {
        if (loginNr == null || loginNr.isBlank()) {
            throw new WebApplicationException("The login number must not be null or blank.");
        }
        double price = req.price();
        if (price == 0) {
            throw new WebApplicationException("The price must not be 0.");
        }
        String displayName = req.displayName();
        if (displayName == null || displayName.isBlank()) {
            throw new WebApplicationException("The display name must not be null or blank.");
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
            throw new WebApplicationException("The symbol identifier must not be null or blank.");
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new WebApplicationException("The minimal warning must not be 0.");
        }

        try {
            Product created = productService.createProductByLoginNr(loginNr, price, displayName, symbolIdentifier, minimalWarning);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("/list/by_login_nr/{loginNr}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProductsByLoginNr(@PathParam("loginNr") String loginNr) {
        if (loginNr == null || loginNr.isBlank()) {
            throw new WebApplicationException("The login number must not be null or blank.");
        }

        try {
            List<Product> data = productService.listProductsByLoginNr(loginNr);
            return Response.status(Response.Status.OK).entity(data).build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("/list/by_food_court_id/{foodCourtId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) {
        if (foodCourtId == null) {
            throw new WebApplicationException("The food court id must not be null.");
        }

        List<Product> data = productService.listProductsByFoodCourtId(foodCourtId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("by_food_court_id/{foodCourtId}")
    public Response createProductByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) ProductRequest req) {
        if (foodCourtId == null) {
            throw new WebApplicationException("The food court id must not be null.");
        }
        UUID productId = req.productId();
        if (productId == null) {
            throw  new WebApplicationException("The product id must not be null.");
        }
        double price = req.price();
        if (price == 0) {
            throw new WebApplicationException("The price must not be 0.");
        }
        String displayName = req.displayName();
        if (displayName == null || displayName.isBlank()) {
            throw new WebApplicationException("The display name must not be null or blank.");
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
            throw new WebApplicationException("The symbol identifier must not be null or blank.");
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new WebApplicationException("The minimal warning must not be 0.");
        }

        try {
            Product created = productService.createProductWithId(productId, foodCourtId, price, displayName, symbolIdentifier, minimalWarning);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (KeyAlreadyExistsException | EntityNotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Path("/list/by_id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProductsById(@PathParam("id") UUID id) {
        if (id == null) {
            throw new WebApplicationException("The product id must not be null.");
        }

        try {
            Product data = productService.getProductById(id);
            return Response.status(Response.Status.OK).entity(data).build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }

    @DELETE
    @Path("by_id/{id}")
    public Response deleteProductById(@PathParam("id") UUID id) {
        if (id == null) {
            throw new WebApplicationException("The product id must not be null.");
        }

        try {
            productService.deleteProductById(id);
            return Response.status(Response.Status.OK).entity(null).build();
        } catch (NotFoundException e) {
            throw new WebApplicationException(e);
        }
    }
}
