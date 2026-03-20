package com.ffb.model.api.request.product;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "price",
        "displayName",
        "symbolIdentifier",
        "minimalWarning"
})
public record ProductRequestSimple(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double price,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String symbolIdentifier,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int minimalWarning
) {
}
