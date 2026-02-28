package com.ffb.model.api.response.cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartItemResponse(UUID id, String displayName, String symbolIdentifier, double price, int count, String extra, List<CartItemResponse> subItems) {

	
}
