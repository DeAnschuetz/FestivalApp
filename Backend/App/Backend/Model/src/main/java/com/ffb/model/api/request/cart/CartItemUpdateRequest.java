package com.ffb.model.api.request.cart;

import java.util.UUID;

public record CartItemUpdateRequest(UUID cartItemId, int itemCount, String extra) {
}
