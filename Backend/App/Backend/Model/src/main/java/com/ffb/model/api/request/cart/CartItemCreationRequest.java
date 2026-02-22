package com.ffb.model.api.request.cart;

import java.util.UUID;

public record CartItemCreationRequest(UUID productId, int itemCount, String extra) {
}
