package com.ffb.model.api.response.notification;

import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.db.object.notification.NotificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FoodOrderNotificationResponse(UUID id, FoodOrderStatus type, NotificationStatus status, String message, LocalDateTime creationTime, LocalDateTime pickupTime) {
}
