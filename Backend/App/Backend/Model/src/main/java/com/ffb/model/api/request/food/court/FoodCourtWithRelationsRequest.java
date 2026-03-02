package com.ffb.model.api.request.food.court;

import java.util.UUID;

public record FoodCourtWithRelationsRequest(UUID foodCourtId, boolean waitingTime, boolean foodOrders) {
}
