package com.ffb.app.service.api.food.order;

import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;

import java.util.List;
import java.util.UUID;

public interface FoodOrderService {
    List<FoodOrder> listAll(boolean withItems);

    FoodOrder getById(UUID id, boolean withItems, boolean withHistory);

    List<FoodOrder> create(String loginNr);

    FoodOrder updateStatus(UUID orderId, FoodOrderStatus status);

    void delete(UUID id);

    List<FoodOrder> listByLoginNr(String loginNr);

    List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status);

    void shareOrder(String loginNr, UUID orderId, String sharedLoginNr);
}
