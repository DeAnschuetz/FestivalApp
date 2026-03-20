package com.ffb.model.api.response.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "price",
        "displayName",
        "symbolIdentifier",
        "minimalWarning",
        "productCount",
        "subProducts"
})
public record ProductResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double price,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String symbolIdentifier,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int minimalWarning,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int productCount,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<ProductResponse> subProducts
) {
}
