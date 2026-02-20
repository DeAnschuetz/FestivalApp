package com.ffb.app.dao.api.cart;

import com.ffb.model.db.objects.cart.Cart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartDao {

    Optional<Cart> findByAccountId(UUID accountId);

    Optional<Cart> findByLoginNr(String loginNr);

    Optional<Cart> findByAccountIdWithItems(UUID accountId);

    Optional<Cart> findByLoginNrWithItems(String loginNr);

    List<Cart> listAllWithItems();

    boolean existsByAccountId(UUID accountId);
}
