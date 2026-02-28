package com.ffb.app.api.exception;

import com.ffb.model.api.response.error.ErrorResponse;
import com.ffb.model.exception.ApiException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

    @Override
    public Response toResponse(ApiException exception) {
        ErrorResponse body = new ErrorResponse(
                "API_ERROR",
                exception.getMessage() == null ? "Request failed" : exception.getMessage()
        );

        return Response.status(exception.getStatus())
                .type(MediaType.APPLICATION_JSON)
                .entity(body)
                .build();
    }

}