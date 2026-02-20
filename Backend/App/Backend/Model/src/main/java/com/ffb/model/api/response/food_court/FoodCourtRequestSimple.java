package com.ffb.model.api.response.food_court;

import java.net.URI;
import java.util.UUID;

public record FoodCourtRequestSimple(UUID accountId, String name, URI imageUri, int waitingTime) {
}
