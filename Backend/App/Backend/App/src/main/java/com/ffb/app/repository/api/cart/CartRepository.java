package com.ffb.app.repository.api.cart;

import com.ffb.model.db.objects.cart.Cart;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends PanacheRepositoryBase<Cart, UUID> {

    Optional<Cart> findByLoginNr(String loginNr);

    Optional<Cart> findByLoginNrWithItems(String loginNr);
}
