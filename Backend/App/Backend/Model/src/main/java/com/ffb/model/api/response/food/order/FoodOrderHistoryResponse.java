package com.ffb.model.api.response.food.order;

import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(requiredProperties = {
        "oldStatus",
        "newStatus",
        "statusChangeTime"
})
public record FoodOrderHistoryResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodOrderStatus oldStatus,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodOrderStatus newStatus,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) LocalDateTime statusChangeTime
) {
}
