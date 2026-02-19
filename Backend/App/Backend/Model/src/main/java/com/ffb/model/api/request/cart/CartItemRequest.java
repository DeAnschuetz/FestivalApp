package com.ffb.model.request.cart;

import java.util.UUID;

public record CartItemRequest(String login_nr, UUID productID, int itemCount, String extra) {


}
