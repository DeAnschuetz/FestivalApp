package com.ffb.app.repository.impl.cart;

import com.ffb.app.repository.api.cart.CartRepository;
import com.ffb.model.db.object.cart.Cart;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CartRepositoryImpl implements CartRepository {

    // TODO Logging

    @Override
    public Optional<Cart> findByLoginNr(String loginNr) {
        return find(
                    "account.ticket.loginNr",
                    loginNr
                )//
                .firstResultOptional()//
        ;
    }
}
