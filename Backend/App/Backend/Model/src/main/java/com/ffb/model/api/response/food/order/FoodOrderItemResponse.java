package com.ffb.model.api.response.food.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = {
        "productId",
        "displayName",
        "iconIdentifier",
        "count",
        "extra",
        "subItems"
})
public record FoodOrderItemResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID productId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String iconIdentifier,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int count,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String extra,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderItemResponse> subItems
) {

}
