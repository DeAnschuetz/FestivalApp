package com.ffb.model.exception;

public class ServiceException extends FestivalFoodException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
