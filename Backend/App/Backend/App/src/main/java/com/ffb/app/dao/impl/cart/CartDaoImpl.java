package com.ffb.app.dao.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.repository.api.cart.CartItemRepository;
import com.ffb.app.repository.api.cart.CartRepository;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CartDaoImpl implements CartDao {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    @Inject
    public CartDaoImpl(CartRepository cartRepo, CartItemRepository cartItemRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public Cart findByLoginNr(String loginNr) throws DaoException {
        return cartRepo.findByLoginNr(loginNr)
                .orElseThrow(() -> new DaoException("Cart not found for login number: " + loginNr))//
        ;
    }

    @Override
    public Cart findByLoginNrWithItems(String loginNr) throws DaoException {
        return cartRepo.findByLoginNrWithItems(loginNr)
                .orElseThrow(() -> new DaoException("Cart not found for login number: " + loginNr))//
        ;
    }

    @Override
    public void persist(Cart cart) {
        cartRepo.persist(cart);
    }

    @Override
    public void persistCartItem(CartItem item) {
        cartItemRepo.persist(item);
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
