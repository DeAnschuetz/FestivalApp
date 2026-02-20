package com.ffb.app.repository.api.foodcourt;

import com.ffb.model.db.objects.foodcourt.Foodcourt;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodcourtRepository extends PanacheRepositoryBase<Foodcourt, UUID> {

    List<Foodcourt> findByAccountId(UUID accountId);

    Optional<Foodcourt> findByIdOptional(UUID id);
}
