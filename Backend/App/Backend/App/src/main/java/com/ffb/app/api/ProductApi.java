package com.ffb.app.api;

import com.ffb.app.service.api.api.product.ProductService;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.product.ProductRequestSimple;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.PartType;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("products")
public class ProductApi {

    @Inject
    JsonWebToken jwt;
    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/all")
    @RolesAllowed("GUEST")
    public Response listAll() {
        List<Product> data = productService.listProducts();
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response createProductByLoginNr(ProductRequestSimple req) throws ApiException {
        String loginNr = jwt.getName();
        double price = req.price();
        if (price == 0) {
            throw new ApiException("The price must not be 0.");
        }
        String displayName = req.displayName();
        if (displayName == null || displayName.isBlank()) {
            throw new ApiException("The display name must not be null or blank.");
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
            throw new ApiException("The symbol identifier must not be null or blank.");
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new ApiException("The minimal warning must not be 0.");
        }

        try {
            Product created = productService.createProductByLoginNr(loginNr, price, displayName, symbolIdentifier, minimalWarning);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response listProductsByLoginNr() throws ApiException {
        String loginNr = jwt.getName();
        List<Product> data = productService.listProductsByLoginNr(loginNr);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Path("list/by_food_court_id/{foodCourtId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("GUEST")
    public Response listProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
        if (foodCourtId == null) {
            throw new ApiException("The food court id must not be null.");
        }

        List<Product> data = productService.listProductsByFoodCourtId(foodCourtId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("by_food_court_id/{foodCourtId}")
    @RolesAllowed("ADMIN")
    public Response createProductByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) ProductRequest req) throws ApiException {
        if (foodCourtId == null) {
            throw new ApiException("The food court id must not be null.");
        }
        UUID productId = req.productId();
        if (productId == null) {
            throw  new ApiException("The product id must not be null.");
        }
        double price = req.price();
        if (price == 0) {
            throw new ApiException("The price must not be 0.");
        }
        String displayName = req.displayName();
        if (displayName == null || displayName.isBlank()) {
            throw new ApiException("The display name must not be null or blank.");
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
            throw new ApiException("The symbol identifier must not be null or blank.");
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new ApiException("The minimal warning must not be 0.");
        }

        Product created;
        try {
            created = productService.createProductWithId(productId, foodCourtId, price, displayName, symbolIdentifier, minimalWarning);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("by_food_court_id/many/{foodCourtId}")
    @RolesAllowed("ADMIN")
    public Response createProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) List<ProductRequest> requests) throws ApiException {
        if (foodCourtId == null) {
            throw new ApiException("The food court id must not be null.");
        }
        List<Product> createdProducts = requests.stream()//
                .map(req -> {
                        UUID productId = req.productId();
                        if (productId == null) {
                            throw  new RuntimeException("The product id must not be null.");
                        }
                        double price = req.price();
                        if (price == 0) {
                            throw new RuntimeException("The price must not be 0.");
                        }
                        String displayName = req.displayName();
                        if (displayName == null || displayName.isBlank()) {
                            throw new RuntimeException("The display name must not be null or blank.");
                        }
                        String symbolIdentifier = req.symbolIdentifier();
                        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
                            throw new RuntimeException("The symbol identifier must not be null or blank.");
                        }
                        int minimalWarning = req.minimalWarning();
                        if (minimalWarning == 0) {
                            throw new RuntimeException("The minimal warning must not be 0.");
                        }
                            try {
                                return productService.createProductWithId(productId, foodCourtId, price, displayName, symbolIdentifier, minimalWarning);
                            } catch (ServiceException e) {
                                throw new RuntimeException(e);
                            }
                        }//
                )//
                .toList()//
        ;

        return Response.status(Response.Status.CREATED).entity(createdProducts).build();
    }

    @GET
    @Path("list/by_id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("GUEST")
    public Response listProductsById(@PathParam("id") UUID id) throws ApiException {
        if (id == null) {
            throw new ApiException("The product id must not be null.");
        }

        Product data = productService.getProductById(id);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @DELETE
    @Path("by_id/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteProductById(@PathParam("id") UUID id) throws ApiException {
        if (id == null) {
            throw new ApiException("The product id must not be null.");
        }

        try {
            productService.deleteProductById(id);
            return Response.status(Response.Status.OK).entity(null).build();
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
    }
}
