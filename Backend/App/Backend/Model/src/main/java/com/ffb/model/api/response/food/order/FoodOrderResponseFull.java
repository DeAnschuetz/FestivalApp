package com.ffb.model.api.response.food.order;

import com.ffb.model.api.response.notification.FoodOrderNotificationResponse;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;

import java.util.List;
import java.util.UUID;

public record FoodOrderResponseFull(UUID id, FoodOrderStatus status, String foodCourtName, int waitingTime, List<FoodOrderItemResponse> orderItems, List<FoodOrderHistoryResponse> history, List<FoodOrderNotificationResponse> notifications) {

}
