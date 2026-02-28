package com.ffb.app.service.api.cart;

import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.response.cart.CartResponseFull;
import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.exception.ServiceException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface CartService {

    CartResponseSimple getCartByLoginNr(String loginNr) throws ServiceException;

    @Transactional
    CartResponseSimple changePrio(String loginNr, boolean newPrio) throws ServiceException;

    @Transactional
    CartResponseSimple removeItemFromCart(String loginNr, UUID cartItemId) throws ServiceException;

    @Transactional
    CartResponseSimple updateCartItemById(String loginNr, CartItemUpdateRequest request) throws ServiceException;

    List<CartResponseFull> listAll();

    @Transactional
    CartResponseSimple addItemToCart(String loginNr, CartItemCreationRequest request) throws ServiceException;
}
