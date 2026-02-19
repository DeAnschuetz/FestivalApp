package com.ffb.model.api.request.cart;

import java.util.UUID;

public record CartItemRequest(String login_nr, UUID productID, int itemCount, String extra) {


}
