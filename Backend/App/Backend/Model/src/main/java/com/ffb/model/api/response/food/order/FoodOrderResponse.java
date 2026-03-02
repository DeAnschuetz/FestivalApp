package com.ffb.model.api.response.food.order;

import java.util.List;
import java.util.UUID;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;



public record FoodOrderResponse(UUID id, FoodOrderStatus status, String foodCourtName, int waitingTime, List<FoodOrderItemResponse> orderItems) {

}
