package com.ffb.app.service.api.food_court;

import com.ffb.model.db.objects.food_court.FoodCourt;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface FoodCourtService {

    FoodCourt getFoodCourtById(UUID id);

    List<FoodCourt> listAll();

    List<FoodCourt> listByAccountId(UUID accountId);

    FoodCourt create(UUID accountId, String name, URI imageUri);

    FoodCourt update(UUID id, UUID accountId, String name, URI imageUri);

    void delete(UUID id);

    FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders);
}
