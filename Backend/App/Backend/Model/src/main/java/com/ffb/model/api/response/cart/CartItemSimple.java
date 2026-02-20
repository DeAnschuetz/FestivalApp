package com.ffb.model.api.response.cart;

import java.util.List;
import java.util.UUID;

public record CartItemSimple(UUID id, String displayName, String symbolIdentifier, double price, int count, String extra, List<CartItemSimple> subItems) {

	
}
