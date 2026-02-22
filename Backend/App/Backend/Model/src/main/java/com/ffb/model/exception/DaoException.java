package com.ffb.model.exception;

import java.io.IOException;

public class DaoException extends FestivalFoodException{


    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, IOException cause) {
        super(message, cause);
    }
}
