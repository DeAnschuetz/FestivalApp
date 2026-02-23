package com.ffb.model.exception;

import jakarta.ws.rs.core.Response;

import java.io.IOException;

public class ApiException extends FestivalFoodException{

    private final Response.Status status;
    
    public ApiException(String message, Response.Status status) {
        super(message);
        this.status = status;
    }

    public ApiException(ServiceException e) {
        super(e.getMessage(), e.getCause());
        this.status = e.getStatus();
    }

    public ApiException(IOException e, Response.Status status) {
        super(e.getMessage(), e.getCause());
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }
}
