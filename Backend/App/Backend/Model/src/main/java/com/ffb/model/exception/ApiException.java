package com.ffb.model.exception;

public class ApiException extends FestivalFoodException{
    
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
