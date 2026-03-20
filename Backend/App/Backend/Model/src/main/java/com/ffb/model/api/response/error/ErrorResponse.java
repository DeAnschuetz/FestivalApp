package com.ffb.model.api.response.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "code",
        "message"
})
public record ErrorResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String code,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String message
) {
}
