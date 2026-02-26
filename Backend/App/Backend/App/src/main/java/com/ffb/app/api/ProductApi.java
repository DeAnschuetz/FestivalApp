package com.ffb.app.api;

import com.ffb.app.service.api.api.product.ProductService;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.product.ProductRequestSimple;
import com.ffb.model.api.response.product.ProductResponse;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.PartType;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("product")
public class ProductApi {

    private final Logger LOG = Logger.getLogger(ProductApi.class);

    @Inject
    JsonWebToken webToken;
    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Path("list/by_food_court_id/{foodCourtId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("GUEST")
    public Response listProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
        if (foodCourtId == null) {
            throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
        }

        List<ProductResponse> data = productService.listProductsByFoodCourtId(foodCourtId);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("GUEST")
    public Response getProductById(@PathParam("id") UUID id) throws ApiException {
        if (id == null) {
            throw new ApiException("The product id must not be null.", Response.Status.BAD_REQUEST);
        }

        ProductResponse data = productService.getProductById(id);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list_all")
    @RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
    public Response listAll() {
        List<ProductResponse> data = productService.listProducts();
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response createProductByLoginNr(ProductRequestSimple req) throws ApiException {
        String loginNr = webToken.getName();
        double price = req.price();
        if (price < 0) {
            throw new ApiException("The price must not be 0.", Response.Status.BAD_REQUEST);
        }
        String displayName = req.displayName();
        if (displayName == null || displayName.isBlank()) {
            throw new ApiException("The display name must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
            throw new ApiException("The symbol identifier must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new ApiException("The minimal warning must not be 0.", Response.Status.BAD_REQUEST);
        }

        try {
            ProductResponse created = productService.createProductByLoginNr(loginNr, price, displayName, symbolIdentifier, minimalWarning);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    public Response listProductsByLoginNr() {
        String loginNr = webToken.getName();
        List<ProductResponse> data = productService.listProductsByLoginNr(loginNr);
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @DELETE
    @Path("by_id/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"FOOD_COURT_WORKER", "ADMIN"})
    public Response deleteProductById(@PathParam("id") UUID id) throws ApiException {
        if (id == null) {
            throw new ApiException("The product id must not be null.", Response.Status.BAD_REQUEST);
        }

        try {
            productService.deleteProductById(id);
            return Response.status(Response.Status.OK).entity(null).build();
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("admin/by_food_court_id/{foodCourtId}")
    @RolesAllowed("ADMIN")
    public Response createProductByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) ProductRequest req) throws ApiException {
        if (foodCourtId == null) {
            throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
        }
        UUID productId = req.id();
        if (productId == null) {
            throw  new ApiException("The product id must not be null.", Response.Status.BAD_REQUEST);
        }
        double price = req.price();
        if (price < 0) {
            throw new ApiException("The price must not be 0.", Response.Status.BAD_REQUEST);
        }
        String displayName = req.displayName();
        if (displayName == null || displayName.isBlank()) {
            throw new ApiException("The display name must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        String symbolIdentifier = req.symbolIdentifier();
        if (symbolIdentifier == null || symbolIdentifier.isBlank()) {
            throw new ApiException("The symbol identifier must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        int minimalWarning = req.minimalWarning();
        if (minimalWarning == 0) {
            throw new ApiException("The minimal warning must not be 0.", Response.Status.BAD_REQUEST);
        }

        ProductResponse created;
        try {
            created = productService.createProductWithId(productId, foodCourtId, price, displayName, symbolIdentifier, minimalWarning);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("admin/by_food_court_id/many/{foodCourtId}")
    @RolesAllowed("ADMIN")
    public Response createProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) List<ProductRequest> requests) throws ApiException {
        if (foodCourtId == null) {
            throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
        }
        List<ProductResponse> createdProducts = requests.stream()//
                .map(req -> {
                        UUID productId = req.id();
                        if (productId == null) {
                            LOG.info("req: " + req);
                            throw  new RuntimeException("The product id must not be null.");
                        }
                    double price = req.price();
                        if (price < 0) {
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
}
