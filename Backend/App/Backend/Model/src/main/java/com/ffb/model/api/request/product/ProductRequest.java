package com.ffb.model.api.request.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "price",
        "displayName",
        "symbolIdentifier",
        "minimalWarning"
})
public record ProductRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double price,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String symbolIdentifier,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int minimalWarning
) {
}
