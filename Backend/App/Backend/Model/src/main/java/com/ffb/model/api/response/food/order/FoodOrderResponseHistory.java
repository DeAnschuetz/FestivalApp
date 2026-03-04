package com.ffb.model.api.response.food.order;

import com.ffb.model.db.object.foodorder.FoodOrderStatus;

import java.util.List;
import java.util.UUID;

public record FoodOrderResponseHistory(UUID id, FoodOrderStatus status, String foodCourtName, int waitingTime, List<FoodOrderItemResponse> orderItems, List<FoodOrderHistoryResponse> history) {
}
