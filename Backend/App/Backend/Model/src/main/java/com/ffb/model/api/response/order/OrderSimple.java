package com.ffb.model.api.response.order;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.objects.foodorder.FoodOrderStatus;



public record OrderSimple(UUID id, FoodOrderStatus status, String foodcourtName, int waitingTime, List<OrderItemSimple> orderItems) {

}
