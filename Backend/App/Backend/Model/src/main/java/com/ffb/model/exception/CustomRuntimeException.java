package com.ffb.model.exception;

import jakarta.ws.rs.core.Response;

public class CustomRuntimeException extends RuntimeException{

    private Response.Status status;

    public CustomRuntimeException(DaoException e, Response.Status status) {
        super(e.getMessage(),e);
        this.status = status;
    }

    public CustomRuntimeException(String message, Response.Status status) {
        super(message);
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }
}
