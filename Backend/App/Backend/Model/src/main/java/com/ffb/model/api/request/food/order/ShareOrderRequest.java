package com.ffb.model.api.request.food.order;

import java.util.UUID;

public record ShareOrderRequest(UUID orderId, String loginNr) {

}
