package com.ffb.model.response.cart;

import java.util.List;

public record CartSimple(boolean hasPrio, double total, List<CartItemSimple> cartItems) {
	
	
}
