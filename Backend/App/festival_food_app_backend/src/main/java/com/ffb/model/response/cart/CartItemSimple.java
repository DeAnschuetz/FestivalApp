package com.ffb.model.response.cart;

import java.util.List;
import java.util.UUID;

public record CartItemSimple(UUID id, String displayName, byte image, double price, int count, String extra, List<CartItemSimple> subItems) {

	
}
