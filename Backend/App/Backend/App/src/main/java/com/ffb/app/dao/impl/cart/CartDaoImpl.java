package com.ffb.app.dao.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.repository.api.cart.CartRepository;
import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.exception.DaoException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CartDaoImpl implements CartDao {

    // TODO Logging

    private final CartRepository cartRepo;

    @Inject
    public CartDaoImpl(CartRepository cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Override
    public Cart findByLoginNr(String loginNr) throws DaoException {
        return cartRepo.findByLoginNr(loginNr)
                .orElseThrow(() -> new DaoException("Cart not found for login number: " + loginNr))//
        ;
    }

    @Override
    public List<Cart> listAll() {
        return cartRepo.listAll();
    }

    @Override
    public void empty(Cart cart) {
        cart.getCartItems().clear();
        cart.setHasPrio(false);
        cart.setTotal(0);
        cartRepo.persist(cart);
    }
}
