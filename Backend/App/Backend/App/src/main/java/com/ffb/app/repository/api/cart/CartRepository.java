package com.ffb.app.repository.api.cart;

import com.ffb.model.db.objects.cart.Cart;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends PanacheRepositoryBase<Cart, UUID> {

    Optional<Cart> findByAccountId(UUID accountId);

    Optional<Cart> findByLoginNr(String loginNr);

    Optional<Cart> findByAccountIdWithItems(UUID accountId);

    Optional<Cart> findByLoginNrWithItems(String loginNr);

    List<Cart> listAllWithItems();

    boolean existsByAccountId(UUID accountId);
}
