package com.ffb.app.service.api.foodcourt;

import com.ffb.model.api.response.foodcourt.FoodcourtRequest;
import com.ffb.model.db.objects.foodcourt.Foodcourt;

import java.util.List;
import java.util.UUID;

public interface FoodcourtService {

    Foodcourt getOrThrow(UUID id);

    List<Foodcourt> listAll();

    List<Foodcourt> listByAccountId(UUID accountId);

    Foodcourt create(FoodcourtRequest req);

    Foodcourt update(UUID id, FoodcourtRequest req);

    void delete(UUID id);

    Foodcourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders);
}
