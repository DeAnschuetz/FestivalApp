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
    public List<FoodCourt> getByAccountId(UUID accountId) {
        return list("accountId", accountId);
    }

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
