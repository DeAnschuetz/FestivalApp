package com.ffb.model.api.response.food.order;

import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import java.time.LocalDateTime;

public record FoodOrderHistoryResponse(FoodOrderStatus oldStatus, FoodOrderStatus newStatus, LocalDateTime statusChangeTime) {
}
