package com.ffb.model.api.response.cart;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "displayName",
        "price",
        "count",
        "extra",
        "subItems"
})
public record CartItemResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String symbolIdentifier,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double price,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int count,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String extra,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<CartItemResponse> subItems
) {

	
}
