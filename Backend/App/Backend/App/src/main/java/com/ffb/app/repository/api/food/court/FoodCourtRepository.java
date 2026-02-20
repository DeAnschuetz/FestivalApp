package com.ffb.app.repository.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodCourtRepository extends PanacheRepositoryBase<FoodCourt, UUID> {

    List<FoodCourt> findByAccountId(UUID accountId);

    Optional<FoodCourt> findByIdOptional(UUID id);
}
