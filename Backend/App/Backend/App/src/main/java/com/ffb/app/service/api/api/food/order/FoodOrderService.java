package com.ffb.app.service.api.api.food.order;

import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import com.ffb.model.exception.ServiceException;

import java.util.List;
import java.util.UUID;

public interface FoodOrderService {
    List<FoodOrder> listAll(boolean withItems);

    FoodOrder getById(UUID id, boolean withItems, boolean withHistory) throws ServiceException;

    List<FoodOrder> create(String loginNr) throws ServiceException;

    FoodOrder updateStatus(UUID orderId, FoodOrderStatus status) throws ServiceException;

    void delete(UUID id) throws ServiceException;

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);

    void shareOrder(String loginNr, UUID orderId, String sharedLoginNr) throws ServiceException;
}
