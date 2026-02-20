package com.ffb.model.api.request.cart;

import java.util.UUID;

public record CartItemRequest(String loginNr, UUID productId, int itemCount, String extra) {


}
