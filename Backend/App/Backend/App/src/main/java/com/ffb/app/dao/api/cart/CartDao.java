package com.ffb.app.dao.api.cart;

import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.exception.DaoException;

import java.util.List;

public interface CartDao {

    Cart findByLoginNr(String loginNr) throws DaoException;

    List<Cart> listAll();

    void empty(Cart cart);
}
