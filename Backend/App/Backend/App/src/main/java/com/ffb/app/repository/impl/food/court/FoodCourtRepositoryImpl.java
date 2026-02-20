package com.ffb.app.repository.impl.food.court;

import com.ffb.app.repository.api.food.court.FoodCourtRepository;
import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtRepositoryImpl implements FoodCourtRepository {

    public List<FoodCourt> findByAccountId(UUID accountId) {
        return list("accountID", accountId);
    }

    public Optional<FoodCourt> findByIdOptional(UUID id) {
        return find(
                    "id",
                        id
                )//
                .firstResultOptional()//
        ;
    }
}
