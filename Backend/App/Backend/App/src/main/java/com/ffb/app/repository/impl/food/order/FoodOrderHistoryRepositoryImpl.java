package com.ffb.app.repository.impl.food.order;

import com.ffb.app.repository.api.food.order.FoodOrderHistoryRepository;
import com.ffb.model.db.object.foodorder.FoodOrderHistory;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodOrderHistoryRepositoryImpl implements FoodOrderHistoryRepository {

    // TODO Logging

    @Override
    public List<FoodOrderHistory> listByFoodOrderId(UUID foodOrderId) {
        return find(
                    "foodOrder.id ORDER BY statusChangeTime DESC",
                    foodOrderId
                )//
                .list()//
        ;
    }
}
