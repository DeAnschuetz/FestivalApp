package com.ffb.app.service.api.api.cart;

import com.ffb.model.api.response.cart.CartResponse;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.exception.ServiceException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface CartService {

    CartResponse getCartByLoginNr(String loginNr) throws ServiceException;

    @Transactional
    CartResponse changePrio(String loginNr, boolean newPrio) throws ServiceException;

    @Transactional
    CartResponse addItemToCart(String loginNr, UUID productId, int itemCount, String extra) throws ServiceException;

    @Transactional
    CartResponse removeItemFromCart(String loginNr, UUID cartItemId) throws ServiceException;

    @Transactional
    CartResponse updateCartItemById(String loginNr, UUID cartItemId, int newItemCount, String newExtra) throws ServiceException;

    List<CartResponse> listAll();
}
