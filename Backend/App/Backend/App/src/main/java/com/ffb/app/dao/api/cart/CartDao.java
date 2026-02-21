package com.ffb.app.dao.api.cart;

import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartDao {

    Cart findByAccountId(UUID accountId) throws DaoException;

    Cart findByLoginNr(String loginNr) throws DaoException;

    Cart findByAccountIdWithItems(UUID accountId) throws DaoException;

    Cart findByLoginNrWithItems(String loginNr) throws DaoException;

    List<Cart> listAllWithItems();

    boolean existsByAccountId(UUID accountId);

    void persist(Cart cart);

    void persistCartItem(CartItem item);
}
