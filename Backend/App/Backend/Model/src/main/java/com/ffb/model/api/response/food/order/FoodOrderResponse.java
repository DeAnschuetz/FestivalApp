package com.ffb.model.api.response.food.order;

import java.util.List;
import java.util.UUID;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "id",
        "status",
        "foodCourtName",
        "waitingTime",
        "orderItems"
})
public record FoodOrderResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodOrderStatus status,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String foodCourtName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int waitingTime,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderItemResponse> orderItems
) {

}
