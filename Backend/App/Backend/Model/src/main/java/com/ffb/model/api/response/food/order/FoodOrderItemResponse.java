package com.ffb.model.api.response.food.order;

import java.util.List;
import java.util.UUID;

public record FoodOrderItemResponse(UUID productID, String displayName, String iconIdentifier, int count, String extra, List<FoodOrderItemResponse> subItems) {

}
