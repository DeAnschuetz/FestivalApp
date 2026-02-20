package com.ffb.app.repository.api.food.order;

import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodOrderRepository extends PanacheRepositoryBase<FoodOrder, UUID> {

    List<FoodOrder> listAllWithItems();

    Optional<FoodOrder> findByIdWithItems(UUID id);

    Optional<FoodOrder> findByIdWithItemsAndHistory(UUID id);

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);
}
