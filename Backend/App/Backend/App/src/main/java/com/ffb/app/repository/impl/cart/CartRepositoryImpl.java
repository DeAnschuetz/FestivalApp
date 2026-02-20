package com.ffb.app.repository.impl.cart;

import com.ffb.app.repository.api.cart.CartRepository;
import com.ffb.model.db.objects.cart.Cart;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CartRepositoryImpl implements CartRepository {

    public Optional<Cart> findByAccountId(UUID accountId) {
        return find("account.id", accountId).firstResultOptional();
    }

    public Optional<Cart> findByAccountIdWithItems(UUID accountId) {
        return find("SELECT DISTINCT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.account.id = ?1",accountId)//
                .firstResultOptional()//
        ;
    }

    public Optional<Cart> findByLoginNrWithItems(String loginNr) {
        return find(
                "SELECT DISTINCT c FROM Cart c " +
                        "LEFT JOIN FETCH c.cartItems " +
                        "WHERE c.account.loginNr = ?1",
                loginNr
        ).firstResultOptional();
    }

    public List<Cart> listAllWithItems() {
        return find("SELECT DISTINCT c FROM Cart c LEFT JOIN FETCH c.cartItems")//
                .list()//
        ;
    }

    public boolean existsByAccountId(UUID accountId) {
        return count("account.id", accountId) > 0;
    }
}
