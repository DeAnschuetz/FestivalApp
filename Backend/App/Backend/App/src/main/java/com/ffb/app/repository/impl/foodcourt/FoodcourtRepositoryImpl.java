package com.ffb.app.repository.impl.foodcourt;

import com.ffb.app.repository.api.foodcourt.FoodcourtRepository;
import com.ffb.model.db.objects.foodcourt.Foodcourt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FoodcourtRepositoryImpl implements FoodcourtRepository {

    public List<Foodcourt> findByAccountId(UUID accountId) {
        return list("accountID", accountId);
    }

    public Optional<Foodcourt> findByIdOptional(UUID id) {
        return find("id", id).firstResultOptional();
    }
}
