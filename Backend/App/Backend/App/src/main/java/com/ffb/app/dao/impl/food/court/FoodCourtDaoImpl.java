package com.ffb.app.dao.impl.food.court;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.repository.impl.food.court.FoodCourtRepositoryImpl;
import com.ffb.app.repository.impl.food.court.ImageRepositoryImpl;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtDaoImpl implements FoodCourtDao {

    private final FoodCourtRepositoryImpl foodCourtRepo;
    private final ImageRepositoryImpl imageRepo;

    public FoodCourtDaoImpl(FoodCourtRepositoryImpl foodCourtRepo, ImageRepositoryImpl imageRepo) {
        this.foodCourtRepo = foodCourtRepo;
        this.imageRepo = imageRepo;
    }

    @Override
    public FoodCourt getByLoginNr(String loginNr) throws DaoException {
        return foodCourtRepo.getByLoginNr(loginNr)
                .orElseThrow(() -> new DaoException("Food court with login number " + loginNr + " not found."))
        ;
    }

    @Override
    public FoodCourt getById(UUID id) throws DaoException {
        return foodCourtRepo.findByIdOptional(id)
                .orElseThrow(() -> new DaoException("Food court with ID " + id + " not found."))
        ;
    }

    @Override
    public void persist(FoodCourt foodCourt) {
        foodCourtRepo.persist(foodCourt);
    }

    @Override
    public void delete(FoodCourt foodCourt) {
        foodCourtRepo.delete(foodCourt);
    }

    @Override
    public List<FoodCourt> listAll() {
        return foodCourtRepo.listAll();
    }

    @Override
    public void persistImage(Image image) {
        imageRepo.persist(image);
    }

    @Override
    public void deleteImage(Image image) {
        imageRepo.delete(image);
    }

    @Override
    public Image getImageByUri(URI uri) throws DaoException {
        return imageRepo.getImageByUri(uri)
                .orElseThrow(() -> new DaoException("Image with URI " + uri + " not found."))
        ;
    }

    @Override
    public Image getImageByID(UUID id) throws DaoException {
        return imageRepo.getImageByID(id)
                .orElseThrow(() -> new DaoException("Image with ID " + id + " not found."))
        ;
    }
}
