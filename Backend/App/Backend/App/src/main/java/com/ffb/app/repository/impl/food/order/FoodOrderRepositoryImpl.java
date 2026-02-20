package com.ffb.app.repository.impl.food.order;

import com.ffb.app.repository.api.food.order.FoodOrderRepository;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FoodOrderRepositoryImpl implements FoodOrderRepository {

    public List<FoodOrder> listAllWithItems() {
        return find(
                "SELECT DISTINCT fo" +
                        "FROM FoodOrder fo" +
                        "LEFT JOIN FETCH fo.foodOrderItems"
                )//
                .list()//
        ;
    }

    public Optional<FoodOrder> findByIdWithItems(UUID id) {
        return find(
                "SELECT DISTINCT fo" +
                        "FROM FoodOrder fo" +
                        "LEFT JOIN FETCH fo.foodOrderItems" +
                        "WHERE fo.id = ?1",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    public Optional<FoodOrder> findByIdWithItemsAndHistory(UUID id) {
        return find("SELECT DISTINCT fo" +
                        "FROM FoodOrder fo" +
                        "LEFT JOIN FETCH fo.foodOrderItems" +
                        "LEFT JOIN FETCH fo.foodOrderHistory" +
                        "WHERE fo.id = ?1",
                    id
                )//
                .firstResultOptional()//
                ;
    }

    public List<FoodOrder> listByLoginNr(String loginNr) {
        return List.of();
    }

    public List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status) {
        return List.of();
    }

}
