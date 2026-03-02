package com.ffb.app.api;

import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.product.ProductRequestSimple;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.api.response.product.ProductResponse;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import org.jboss.resteasy.reactive.PartType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
@Path("product")
public class ProductEndpointImpl {

    // TODO Logging
    private final Logger LOG = LoggerFactory.getLogger(ProductEndpointImpl.class);

    @Inject
    JsonWebToken webToken;
    private final ProductService productService;

    @Inject
    public ProductEndpointImpl(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Path("list/by_food_court_id/{foodCourtId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("GUEST")
    @Operation(summary = "List Products by Food Court ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Products found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.ARRAY)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response listProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId) throws ApiException {
        LOG.info("listing products by foodCourtId {" + foodCourtId + "}");
        if (foodCourtId == null) {
            LOG.error("foodCourtId is null");
            throw new ApiException("The food court id must not be null.", Response.Status.BAD_REQUEST);
        }

        List<ProductResponse> data = productService.listProductsByFoodCourtId(foodCourtId);
        LOG.info("found " + data.size() + " products");
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("GUEST")
    @Operation(summary = "Get a Product by ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Product found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response getProductById(@PathParam("id") UUID id) throws ApiException {
        LOG.info("getting product by id {" + id + "}");
        if (id == null) {
            LOG.info("productId is null");
            throw new ApiException("The product id must not be null.", Response.Status.BAD_REQUEST);
        }

        ProductResponse data = productService.getProductById(id);
        LOG.info("got product {" + data.id() + "}");
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list_all")
    @RolesAllowed({"GUEST", "FOOD_COURT_WORKER", "ADMIN"})
    @Operation(summary = "List all Products")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Products found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.ARRAY)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response listAll() {
        LOG.info("getting all products");
        List<ProductResponse> data = productService.listProducts();
        LOG.info("found " + data.size() + " products");
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    @Operation(summary = "Create a Product for the currently logged-in Food Court Worker Account")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Product created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response createProduct(ProductRequestSimple request) throws ApiException {
        LOG.info("creating product for logged-in account" + webToken.getName());
        String loginNr = webToken.getName();
        if (request.price() < 0) {
            LOG.error("price is <0");
            throw new ApiException("The price must not be 0.", Response.Status.BAD_REQUEST);
        }
        if (request.displayName() == null || request.displayName().isBlank()) {
            LOG.error("displayName is null or empty");
            throw new ApiException("The display displayName must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        if (request.symbolIdentifier() == null || request.symbolIdentifier().isBlank()) {
            LOG.error("symbolIdentifier is null or empty");
            throw new ApiException("The symbol identifier must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        if (request.minimalWarning() == 0) {
            LOG.error("minimalWarning is <0");
            throw new ApiException("The minimal warning must not be 0.", Response.Status.BAD_REQUEST);
        }

        ProductResponse created;
        try {
            created = productService.createProductByLoginNr(loginNr, request);
        } catch (ServiceException e) {
            LOG.error("could not create product", e);
            throw new ApiException(e);
        }
        LOG.info("created product {" + created.id() + "} for logged-in account" + webToken.getName());
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("FOOD_COURT_WORKER")
    @Operation(summary = "List Products for the currently logged-in Food Court Worker Account")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Products found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.ARRAY)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response listProducts() {
        LOG.info("listing products for logged-in account" + webToken.getName());
        String loginNr = webToken.getName();
        List<ProductResponse> data = productService.listProductsByLoginNr(loginNr);
        LOG.info("found " + data.size() + " products");
        return Response.status(Response.Status.OK).entity(data).build();
    }

    @DELETE
    @Path("by_id/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"FOOD_COURT_WORKER", "ADMIN"})
    @Operation(summary = "Delete a Product by its ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Product deleted",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response deleteProductById(@PathParam("id") UUID id) throws ApiException {
        LOG.info("deleting product {" + id + "}");
        if (id == null) {
            LOG.error("productId is null");
            throw new ApiException("The product id must not be null.", Response.Status.BAD_REQUEST);
        }

        try {
            productService.deleteProductById(id);
            return Response.status(Response.Status.OK).entity("Product {" + id + "} deleted.").build();
        } catch (ServiceException e) {
            LOG.error("could not delete product {" + id + "}", e);
            throw new ApiException(e);
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("admin/by_food_court_id/{foodCourtId}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Create a Product for a given Food Court")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Product created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response createProductByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) ProductRequest request) throws ApiException {
        LOG.info("creating product for {" + foodCourtId + "}");
        if (foodCourtId == null) {
            LOG.error("foodCourtId is null");
            throw new ApiException("The foodCourtId must not be null.", Response.Status.BAD_REQUEST);
        }
        if (request.id() == null) {
            LOG.error("id is null");
            throw  new ApiException("The productId must not be null.", Response.Status.BAD_REQUEST);
        }
        if (request.price() <= 0) {
            LOG.error("price is <= 0");
            throw new ApiException("The price must not be < 0.", Response.Status.BAD_REQUEST);
        }
        if (request.displayName() == null || request.displayName().isBlank()) {
            LOG.error("displayName is null or empty");
            throw new ApiException("The displayName must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        if (request.symbolIdentifier() == null || request.symbolIdentifier().isBlank()) {
            LOG.error("symbolIdentifier is null or empty");
            throw new ApiException("The symbolIdentifier must not be null or blank.", Response.Status.BAD_REQUEST);
        }
        if (request.minimalWarning() <= 0) {
            LOG.error("minimalWarning is  <= 0");
            throw new ApiException("The minimalWarning must not be < 0.", Response.Status.BAD_REQUEST);
        }

        ProductResponse created;
        try {
            created = productService.createProductWithId(foodCourtId, request);
        } catch (ServiceException e) {
            LOG.error("could no create product", e);
            throw new ApiException(e);
        }
        LOG.info("created product {" + created.id() + "} for {" + foodCourtId + "}");
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("admin/by_food_court_id/many/{foodCourtId}")
    @RolesAllowed("ADMIN")
    @Operation(summary = "Create multiple Products for a given Food Court")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Products created (invalid items are skipped)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ProductResponse.class, type = SchemaType.ARRAY)
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response createProductsByFoodCourtId(@PathParam("foodCourtId") UUID foodCourtId, @PartType(MediaType.APPLICATION_JSON) List<ProductRequest> requests) throws ApiException {
        LOG.info("creating " + requests.size() + "products");
        if (foodCourtId == null) {
            LOG.error("foodCourtId is null");
            throw new ApiException("The food courtId must not be null.", Response.Status.BAD_REQUEST);
        }
        List<ProductResponse> createdProducts = requests.stream()//
                .map(request -> {
                            LOG.info("req: " + request);
                            if (request.id() == null) {
                                LOG.error("requestId is null");
                                return null;
                            }
                            if (request.price() <= 0) {
                                LOG.error("price is <= 0");
                                return null;
                            }
                            if (request.displayName() == null || request.displayName().isBlank()) {
                                LOG.error("displayName is null or empty");
                                return null;
                            }
                            if (request.symbolIdentifier() == null || request.symbolIdentifier().isBlank()) {
                                LOG.error("symbolIdentifier is null or empty");
                                return null;
                            }
                            if (request.minimalWarning() <= 0) {
                                LOG.error("minimalWarning is <= 0");
                                return null;
                            }
                            ProductResponse created;
                            try {
                                created = productService.createProductWithId(foodCourtId, request);
                            } catch (ServiceException e) {
                                LOG.error(e.getMessage());
                                return null;
                            }
                            LOG.info("created product {" + created.id() + "} for foodCourt {" + foodCourtId + "}");
                            return created;
                        }//
                )//
                .filter(Objects::nonNull)//
                .toList()//
        ;
        LOG.info("created " + createdProducts.size() + " products");
        return Response.status(Response.Status.CREATED).entity(createdProducts).build();
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("update/count/{productId}/{newCount}")
    @RolesAllowed({"ADMIN", "WORKER"})
    @Operation(summary = "Update the Count of a Product by its ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Count Updated",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response updateProductCount(@PathParam("productId") UUID productId, @PathParam("newCount") int newCount) throws ApiException {
        LOG.info("updating productId={{}} with count={} ", productId, newCount);
        try {
            productService.updateProductCount(productId, newCount);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }

        return Response.status(Response.Status.CREATED).entity("Product count was changed").build();

    }

}
