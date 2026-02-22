package com.ffb.app.repository.impl.food.order;

import com.ffb.app.repository.api.food.order.FoodOrderItemRepository;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodOrderItemRepositoryImpl implements FoodOrderItemRepository {

    @Override
    public List<FoodOrderItem> listByFoodOrderId(UUID foodOrderId) {
        return find(
                    "foodOrder.id",
                    foodOrderId
                )//
                .list()//
        ;
    }
}
