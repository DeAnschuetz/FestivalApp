package com.ffb.app.dao.api.food.order;

import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderHistory;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.UUID;

public interface FoodOrderDao {

    List<FoodOrder> listAllWithItems();

    FoodOrder findByIdWithItems(UUID id) throws DaoException;

    FoodOrder findByIdWithItemsAndHistory(UUID id) throws DaoException;

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
