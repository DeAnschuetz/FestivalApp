package com.ffb.app.dao.impl.food.court;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.repository.impl.food.court.FoodCourtRepositoryImpl;
import com.ffb.app.repository.impl.food.court.ImageRepositoryImpl;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.util.List;
import java.util.Optional;
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
    public Optional<FoodCourt> getByLoginNr(String loginNr) {
        return foodCourtRepo.getByLoginNr(loginNr);
    }

    @Override
    public Optional<FoodCourt> getById(UUID id) {
        return foodCourtRepo.findByIdOptional(id);
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
    public Optional<Image> getImageByUri(URI uri) {
        return imageRepo.getImageByUri(uri);
    }

    @Override
    public Optional<Image> getImageByID(UUID id) {
        return imageRepo.getImageByID(id);
    }
}
