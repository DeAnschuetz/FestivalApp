package com.ffb.app.service.api.cart;

import com.ffb.model.api.request.cart.CartItemRequest;
import com.ffb.model.api.response.cart.CartSimple;

import java.util.UUID;

public interface CartService {

    CartSimple getCartByLoginNr(String loginNr);

    CartSimple changePrio(String loginNr, boolean newPrio);

    CartSimple addItemToCart(CartItemRequest request);

    CartSimple removeItemFromCart(String loginNr, UUID cartItemId);
}
