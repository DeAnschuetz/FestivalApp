package com.ffb.model.api.response.cart;

import java.util.List;

public record CartResponseSimple(boolean hasPrio, double total, List<CartItemResponse> cartItems) {
	
	
}
