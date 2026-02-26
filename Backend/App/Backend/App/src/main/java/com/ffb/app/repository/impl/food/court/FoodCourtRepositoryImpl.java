package com.ffb.app.repository.impl.food.court;

import com.ffb.app.repository.api.food.court.FoodCourtRepository;
import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtRepositoryImpl implements FoodCourtRepository {

    @Override
    public Optional<FoodCourt> getById(UUID id) {
        return find(
                    "id",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public Optional<FoodCourt> getByLoginNr(String loginNr) {
        return find(
                    "account.ticket.loginNr",
                    loginNr
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public Optional<FoodCourt> getByIdWithWaitingTimeAndFoodOrders(UUID id) {
        return find(
                    "SELECT fc " +
                    "FROM FoodCourt fc " +
                    "LEFT JOIN FETCH fc.waitingTime " +
                    "LEFT JOIN FETCH  fc.foodOrders" +
                    "WHERE fc.id = ?id",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public Optional<FoodCourt> getByIdWithWaitingTime(UUID id) {
        return find(
                "SELECT fc " +
                    "FROM FoodCourt fc " +
                    "LEFT JOIN FETCH fc.waitingTime " +
                    "WHERE fc.id = ?id",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public Optional<FoodCourt> getByIdWithFoodOrders(UUID id) {
        return find(
                "SELECT fc " +
                    "FROM FoodCourt fc " +
                    "LEFT JOIN FETCH  fc.foodOrders" +
                    "WHERE fc.id = ?id",
                    id
                )//
                .firstResultOptional()//
        ;
    }

}
