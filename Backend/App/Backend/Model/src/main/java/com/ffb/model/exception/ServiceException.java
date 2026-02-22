package com.ffb.model.exception;

import java.io.IOException;

public class ServiceException extends FestivalFoodException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, IOException cause) {
        super(message, cause);
    }

    public ServiceException(DaoException exception) {
        super(exception.getMessage(), exception);
    }
}
