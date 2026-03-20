package com.ffb.model.api.response.notification;

import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.db.object.notification.NotificationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "type",
        "status",
        "message",
        "creationTime",
        "pickupTime"
})
public record FoodOrderNotificationResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodOrderStatus type,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) NotificationStatus status,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String message,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) LocalDateTime creationTime,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) LocalDateTime pickupTime
) {
}
