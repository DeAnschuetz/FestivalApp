package com.ffb.model.api.response.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(boolean hasPrio, double total, List<CartItemResponse> cartItems) {
	
	
}
