package com.ffb.model.response.order;

import java.util.List;
import java.util.UUID;

import com.ffb.model.objects.foodorder.FoodOrderStatus;

public record OrderSimple(UUID id, FoodOrderStatus status, String foodcourtName, int waitingTime, List<OrderItemSimple> orderItems) {

}
