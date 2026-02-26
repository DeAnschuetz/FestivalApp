package com.ffb.model.api.response.order;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.objects.foodorder.FoodOrderStatus;



public record FoodOrderResponse(UUID id, FoodOrderStatus status, String foodCourtName, int waitingTime, List<FoodOrderItemResponse> orderItems) {

}
