package com.ffb.model.api.response.food_court;

import java.util.UUID;

public record FoodCourtResponse(UUID id, String name, int waitingTime) {
}
