package com.ffb.app.repository.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.Optional;
import java.util.UUID;

public interface FoodCourtRepository extends PanacheRepositoryBase<FoodCourt, UUID> {

    Optional<FoodCourt> getById(UUID id);

    Optional<FoodCourt> getByLoginNr(String loginNr);

    Optional<FoodCourt> getByIdWithWaitingTimeAndFoodOrders(UUID id);

    Optional<FoodCourt> getByIdWithWaitingTime(UUID id);

    Optional<FoodCourt> getByIdWithFoodOrders(UUID id);
}
