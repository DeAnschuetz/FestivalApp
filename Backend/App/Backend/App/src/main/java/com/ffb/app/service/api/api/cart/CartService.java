package com.ffb.app.service.api.api.cart;

import com.ffb.model.api.response.cart.CartSimple;
import com.ffb.model.exception.ServiceException;

import java.util.UUID;

public interface CartService {

    CartSimple getCartByLoginNr(String loginNr) throws ServiceException;

    CartSimple changePrio(String loginNr, boolean newPrio) throws ServiceException;

    CartSimple addItemToCart(String loginNr, UUID productId, int itemCount, String extra) throws ServiceException;

    CartSimple removeItemFromCart(String loginNr, UUID cartItemId) throws ServiceException;

    CartSimple updateCartItemById(String loginNr, UUID cartItemId, int itemCount, String extra) throws ServiceException;
}
