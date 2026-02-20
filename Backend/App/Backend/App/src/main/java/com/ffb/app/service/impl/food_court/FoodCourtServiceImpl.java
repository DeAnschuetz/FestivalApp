package com.ffb.app.service.impl.food_court;

import com.ffb.app.repository.api.food_court.FoodCourtRepository;
import com.ffb.app.service.api.food_court.FoodCourtService;
import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtServiceImpl implements FoodCourtService {

    FoodCourtRepository foodCourtRepo;

    @Inject
    public FoodCourtServiceImpl(FoodCourtRepository foodcourtRepo) {
        this.foodCourtRepo = foodcourtRepo;

    }

    public FoodCourt getFoodCourtById(UUID id) throws EntityNotFoundException {
        return foodCourtRepo.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Foodcourt not found: " + id));
    }

    public List<FoodCourt> listAll() {
        return foodCourtRepo.listAll();
    }

    public List<FoodCourt> listByAccountId(UUID accountId) {
        return foodCourtRepo.findByAccountId(accountId);
    }

    @Transactional
    public FoodCourt create(UUID accountId, String name, URI imageUri) {
        FoodCourt fc = new FoodCourt(
                UUID.randomUUID(),
                accountId,
                name,
                imageUri
        );
        foodCourtRepo.persist(fc);
        return fc;
    }

    @Transactional
    public FoodCourt update(UUID id, UUID accountId, String name, URI imageUri) throws EntityNotFoundException {
        FoodCourt fc = getFoodCourtById(id);

        fc.setAccountID(accountId);
        fc.setDisplayName(name);
        fc.setImageURI(imageUri);
        return fc;
    }

    @Transactional
    public void delete(UUID id) throws EntityNotFoundException {
        FoodCourt fc = getFoodCourtById(id);
        foodCourtRepo.delete(fc);
    }

    /**
     * Optional: force-load relations for serialization/use in resource layer.
     * Only do this inside a transaction.
     */
    @Transactional
    public FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders) throws EntityNotFoundException  {
        FoodCourt fc = getFoodCourtById(id);
        return fc;
    }
}
