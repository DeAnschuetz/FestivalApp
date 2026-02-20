package com.ffb.app.service.impl.foodcourt;

import com.ffb.app.repository.api.foodcourt.FoodcourtRepository;
import com.ffb.app.service.api.foodcourt.FoodcourtService;
import com.ffb.model.api.response.foodcourt.FoodcourtRequest;
import com.ffb.model.db.objects.foodcourt.Foodcourt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodcourtServiceImpl implements FoodcourtService {

    FoodcourtRepository foodcourtRepo;

    @Inject
    public FoodcourtServiceImpl(FoodcourtRepository foodcourtRepo) {
        this.foodcourtRepo = foodcourtRepo;

    }

    public Foodcourt getOrThrow(UUID id) throws EntityNotFoundException {
        return foodcourtRepo.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Foodcourt not found: " + id));
    }

    public List<Foodcourt> listAll() {
        return foodcourtRepo.listAll();
    }

    public List<Foodcourt> listByAccountId(UUID accountId) {
        return foodcourtRepo.findByAccountId(accountId);
    }

    @Transactional
    public Foodcourt create(FoodcourtRequest req) {
        Foodcourt fc = new Foodcourt(
                UUID.randomUUID(),
                req.accountId(),
                req.name(),
                req.imageUri()
        );
        foodcourtRepo.persist(fc);
        return fc;
    }

    @Transactional
    public Foodcourt update(UUID id, FoodcourtRequest req) throws EntityNotFoundException {
        Foodcourt fc = getOrThrow(id);

        if (req.accountId() != null) fc.setAccountID(req.accountId());
        if (req.name() != null) fc.setDisplayName(req.name());
        if (req.imageUri() != null) fc.setImageURI(req.imageUri());

        return fc;
    }

    @Transactional
    public void delete(UUID id) throws EntityNotFoundException {
        Foodcourt fc = getOrThrow(id);
        foodcourtRepo.delete(fc);
    }

    /**
     * Optional: force-load relations for serialization/use in resource layer.
     * Only do this inside a transaction.
     */
    @Transactional
    public Foodcourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders) throws EntityNotFoundException  {
        Foodcourt fc = getOrThrow(id);

        if (waitingTime && fc.getWaitingTime() != null) {
            // touch to initialize
            fc.getWaitingTime().getId();
        }
        if (foodOrders && fc.getFoodOrder() != null) {
            fc.getFoodOrder().size();
        }
        return fc;
    }
}
