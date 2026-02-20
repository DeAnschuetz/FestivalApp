package com.ffb.app.dao.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.repository.api.cart.CartRepository;
import com.ffb.model.db.objects.cart.Cart;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CartDaoImpl implements CartDao {

    private final CartRepository cartRepo;

    @Inject
    public CartDaoImpl(CartRepository cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Override
    public Optional<Cart> findByAccountId(UUID accountId) {
        return cartRepo.findByAccountId(accountId);
    }

    @Override
    public Optional<Cart> findByLoginNr(String loginNr) {
        return cartRepo.findByLoginNr(loginNr);
    }

    @Override
    public Optional<Cart> findByAccountIdWithItems(UUID accountId) {
        return cartRepo.findByAccountIdWithItems(accountId);
    }

    @Override
    public Optional<Cart> findByLoginNrWithItems(String loginNr) {
        return cartRepo.findByLoginNrWithItems(loginNr);
    }

    @Override
    public List<Cart> listAllWithItems() {
        return cartRepo.listAllWithItems();
    }

    @Override
    public boolean existsByAccountId(UUID accountId) {
        return cartRepo.existsByAccountId(accountId);
    }
}
