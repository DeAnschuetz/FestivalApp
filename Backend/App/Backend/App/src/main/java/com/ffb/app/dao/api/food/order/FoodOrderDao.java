package com.ffb.app.dao.api.food.order;

import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.UUID;

public interface FoodOrderDao {

    FoodOrder getById(UUID id) throws DaoException;

    FoodOrder getByIdWithItems(UUID id) throws DaoException;

    FoodOrder getByIdWithItemsAndHistory(UUID id) throws DaoException;

    List<FoodOrder> listAll();

    List<FoodOrder> listAllByStatus(FoodOrderStatus status);

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);

    List<FoodOrder> listByFoodCourtId(UUID foodCourtId);

    List<FoodOrder> listByFoodCourtIdAndStatus(UUID foodCourtId, FoodOrderStatus status);

    void persist(FoodOrder foodOrder);

    void delete(FoodOrder foodOrder);
}
