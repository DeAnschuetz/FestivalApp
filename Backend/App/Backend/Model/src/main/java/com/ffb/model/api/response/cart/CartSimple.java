package com.ffb.model.api.response.cart;

import java.util.List;

public record CartSimple(boolean hasPrio, double total, List<CartItemSimple> cartItems) {
	
	
}
