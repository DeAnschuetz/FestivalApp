package com.ffb.app.dao.impl.food.court;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.repository.api.food.court.FoodCourtRepository;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtDaoImpl implements FoodCourtDao {

    // TODO Logging

    private final FoodCourtRepository foodCourtRepo;

    @Inject
    public FoodCourtDaoImpl(FoodCourtRepository foodCourtRepo) {
        this.foodCourtRepo = foodCourtRepo;
    }

    @Override
    public FoodCourt getById(UUID id) throws DaoException {
        return foodCourtRepo.getById(id)//
                .orElseThrow(() -> new DaoException("Food court with ID " + id + " not found."))//
        ;
    }

    @Override
    public FoodCourt getByLoginNr(String loginNr) throws DaoException {
        return foodCourtRepo.getByLoginNr(loginNr)//
                .orElseThrow(() -> new DaoException("Food court with login number " + loginNr + " not found."))//
        ;
    }

    @Override
    public byte[] getImageById(UUID id) throws DaoException {
        return foodCourtRepo.getById(id)//
                .orElseThrow(() -> new DaoException("Image with ID " + id + " not found."))//
                .getImage()//
        ;
    }

    @Override
    public List<FoodCourt> listAll() {
        return foodCourtRepo.listAll();
    }

    @Override
    public void addImage(String loginNr, PushbackInputStream inputData) throws DaoException {
        try {
            byte[] bytes = inputData.readAllBytes();
            if (bytes.length == 0) {
                throw new DaoException("Uploaded image is empty.");
            }
            FoodCourt foodcourt = foodCourtRepo.getByLoginNr(loginNr)
                    .orElseThrow(() -> new DaoException("Food court with login number " + loginNr + " not found."))
            ;
            foodcourt.setImage(bytes);
            foodCourtRepo.persist(foodcourt);
        } catch (IOException e) {
            throw new DaoException("Failed to read image data from stream.", e);
        }
    }

    @Override
    public void persist(FoodCourt foodCourt) {
        foodCourtRepo.persist(foodCourt);
    }

    @Override
    public void delete(FoodCourt foodCourt) {
        foodCourtRepo.delete(foodCourt);
    }
}
