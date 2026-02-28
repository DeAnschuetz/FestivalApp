package com.ffb.app.api;

import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.api.request.product.ProductLinkRequest;
import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.exception.ApiException;
import com.ffb.model.exception.ServiceException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.PartType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@ApplicationScoped
@Path("products/assignments")
public class ProductAssignmentApi {

    // TODO Logging
    private final Logger LOG = LoggerFactory.getLogger(ProductAssignmentApi.class);


    private final ProductService productService;

    @Inject
    public ProductAssignmentApi(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"FOOD_COURT_WORKER", "ADMIN"})
    @Operation(summary = "Create a Main/Sub-Product Assignment (by IDs)")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Assignment created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(type = SchemaType.BOOLEAN)
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
            @APIResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response createAssignmentForIds( @PartType(MediaType.APPLICATION_JSON) ProductLinkRequest request) throws ApiException {
        if (request.mainProductId() == null) {
            throw new ApiException("The main product id must not be null.", Response.Status.BAD_REQUEST);
        }
        if (request.subProductId() == null) {
            throw new ApiException("The sub product id must not be null.", Response.Status.BAD_REQUEST);
        }

        boolean result;
        try {
            result = productService.createAssignment(request);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @DELETE
    @Path("by_id/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"FOOD_COURT_WORKER", "ADMIN"})
    @Operation(summary = "Delete an Assignment by its ID")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Assignment deleted"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Assignment not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response deleteAssignmentById(@PathParam("id") UUID id) throws ApiException {
        if (id == null) {
            throw new ApiException("The assignment id must not be null.", Response.Status.BAD_REQUEST);
        }

        try {
            productService.deleteAssignmentById(id);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return Response.status(Response.Status.OK).entity(null).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"FOOD_COURT_WORKER", "ADMIN"})
    @Operation(summary = "Delete an assignment by Main/Sub-ProductIds")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Assignment deleted"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid Request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Assignment not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ErrorResponse.class, type = SchemaType.OBJECT)
                    )
            ),
            @APIResponse(responseCode = "401", description = "Not Authorized"),
            @APIResponse(responseCode = "403", description = "Not Allowed")
    })
    public Response deleteAssignmentByMainSubProductIds( @PartType(MediaType.APPLICATION_JSON) ProductLinkRequest request) throws ApiException {
        if (request.mainProductId() == null) {
            throw new ApiException("The main product id must not be null.", Response.Status.BAD_REQUEST);
        }
        if (request.subProductId() == null) {
            throw new ApiException("The sub product id must not be null.", Response.Status.BAD_REQUEST);
        }

        try {
            productService.deleteAssignmentByPair(request);
        } catch (ServiceException e) {
            throw new ApiException(e);
        }
        return Response.status(Response.Status.OK).entity(null).build();
    }
}
