package com.ffb.app.repository.api.food.order;

import com.ffb.model.db.object.foodorder.FoodOrderItem;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.UUID;

public interface FoodOrderItemRepository extends PanacheRepositoryBase<FoodOrderItem, UUID> {
}
