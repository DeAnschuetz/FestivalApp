package com.ffb.model.exception;

import java.io.Serial;

public class FestivalFoodException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public FestivalFoodException(String message) {
        super(message);
    }

    public FestivalFoodException(String message, Throwable cause) {
        super(message, cause);
    }
}
