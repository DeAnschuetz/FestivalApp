package com.ffb.model.exception;

import jakarta.ws.rs.core.Response;

import java.io.IOException;

public class ServiceException extends FestivalFoodException {

    private final Response.Status status;

    public ServiceException(String message, Response.Status status) {
        super(message);
        this.status = status;
    }

    public ServiceException(String message, IOException cause, Response.Status status) {
        super(message, cause);
        this.status = status;
    }

    public ServiceException(Exception cause, Response.Status status) {
        super(cause.getMessage(), cause);
        this.status = status;
    }

    public ServiceException(CustomRuntimeException cause) {
        super(cause.getMessage(), cause.getCause());
        this.status = cause.getStatus();
    }

    public Response.Status getStatus() {
        return status;
    }
}
