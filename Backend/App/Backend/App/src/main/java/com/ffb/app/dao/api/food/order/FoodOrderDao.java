package com.ffb.app.dao.api.food.order;

import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderHistory;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodOrderDao {

    List<FoodOrder> listAllWithItems();

    Optional<FoodOrder> findByIdWithItems(UUID id);

    Optional<FoodOrder> findByIdWithItemsAndHistory(UUID id);

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);

    List<FoodOrderHistory> listHistoryByFoodOrderId(UUID foodOrderId);

    List<FoodOrderItem> listItemsByFoodOrderId(UUID foodOrderId);

    List<FoodOrder> listAll();

    FoodOrder findById(UUID id);

    void persistHistory(FoodOrderHistory history);

    void persist(FoodOrder foodOrder);

    void delete(FoodOrder foodOrder);

    void persistItem(FoodOrderItem foodOrderItem);
}
