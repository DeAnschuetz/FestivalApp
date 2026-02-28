package com.ffb.model.api.response.cart;

import com.ffb.model.api.response.account.AccountResponse;

import java.util.List;

public record CartResponseFull(AccountResponse account, boolean hasPrio, double total, List<CartItemResponse> cartItems) {
}
