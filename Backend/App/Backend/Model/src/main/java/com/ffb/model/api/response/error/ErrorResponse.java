package com.ffb.model.api.response.error;

public record ErrorResponse(
        String code,
        String message
) {
}
