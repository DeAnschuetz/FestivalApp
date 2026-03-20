package com.ffb.model.api.response.food.order;

import com.ffb.model.api.response.notification.FoodOrderNotificationResponse;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "status",
        "foodCourtName",
        "waitingTime",
        "orderItems",
        "history",
        "notifications"
})
public record FoodOrderResponseFull(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodOrderStatus status,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String foodCourtName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int waitingTime,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderItemResponse> orderItems,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderHistoryResponse> history,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderNotificationResponse> notifications
) {

}
