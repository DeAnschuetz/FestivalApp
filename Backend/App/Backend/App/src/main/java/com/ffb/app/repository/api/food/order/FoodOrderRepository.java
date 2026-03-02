package com.ffb.app.repository.api.food.order;

import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodOrderRepository extends PanacheRepositoryBase<FoodOrder, UUID> {

    Optional<FoodOrder> getById(UUID id);

    Optional<FoodOrder> getByIdWithItems(UUID id);

    Optional<FoodOrder> getByIdWithItemsAndHistory(UUID id);

    List<FoodOrder> listAll();

    List<FoodOrder> listAllByStatus(FoodOrderStatus status);

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);

    List<FoodOrder> listByFoodCourtId(UUID foodCourtId);

    List<FoodOrder> listByFoodCourtIdAndStatus(UUID foodCourtId, FoodOrderStatus status);

}
