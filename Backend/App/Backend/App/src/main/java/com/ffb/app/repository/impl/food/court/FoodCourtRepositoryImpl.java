package com.ffb.app.repository.impl.food.court;

import com.ffb.app.repository.api.food.court.FoodCourtRepository;
import com.ffb.model.db.object.food_court.FoodCourt;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtRepositoryImpl implements FoodCourtRepository {

    // TODO Logging

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
}
