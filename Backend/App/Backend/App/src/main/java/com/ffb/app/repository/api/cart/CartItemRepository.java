package com.ffb.app.repository.api.cart;

import com.ffb.model.db.object.cart.CartItem;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;


public interface CartItemRepository extends PanacheRepositoryBase<CartItem, UUID> {
}
