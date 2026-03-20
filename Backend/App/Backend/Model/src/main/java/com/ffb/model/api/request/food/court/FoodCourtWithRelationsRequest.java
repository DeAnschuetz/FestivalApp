package com.ffb.model.api.request.food.court;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "foodCourtId",
        "waitingTime",
        "foodOrders"
})
public record FoodCourtWithRelationsRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID foodCourtId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean waitingTime,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean foodOrders
) {
}
