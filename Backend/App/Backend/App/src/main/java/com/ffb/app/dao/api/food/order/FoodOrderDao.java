package com.ffb.app.dao.api.food.order;

import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderHistory;
import com.ffb.model.db.object.foodorder.FoodOrderItem;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.UUID;

public interface FoodOrderDao {

    FoodOrder getById(UUID id) throws DaoException;

    FoodOrder getByIdWithItems(UUID id) throws DaoException;

    FoodOrder getByIdWithItemsAndHistory(UUID id) throws DaoException;

    List<FoodOrder> listAll();

    List<FoodOrder> listAllWithItems();

    List<FoodOrder> listAllByStatus(FoodOrderStatus status);

    List<FoodOrder> listAllWithItemsByStatus(FoodOrderStatus status);

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrWithItems(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);

    List<FoodOrder> listByLoginNrAndStatusWithItems(String loginNr, FoodOrderStatus status);

    List<FoodOrder> listByFoodCourtId(UUID foodCourtId);

    List<FoodOrder> listByFoodCourtIdWithItems(UUID foodCourtId);

    List<FoodOrder> listByFoodCourtIdAndStatus(UUID foodCourtId, FoodOrderStatus status);

    List<FoodOrder> listByFoodCourtIdAndStatusWithItems(UUID foodCourtId, FoodOrderStatus status);

    List<FoodOrderItem> listItemsByFoodOrderId(UUID foodOrderId);

    List<FoodOrderHistory> listHistoryByFoodOrderId(UUID foodOrderId);

    void persist(FoodOrder foodOrder);

    void persistHistory(FoodOrderHistory history);

    void persistItem(FoodOrderItem foodOrderItem);

    void delete(FoodOrder foodOrder);
}
