package com.ffb.app.dao.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodCourtDao {

    Optional<FoodCourt> getByLoginNr(String loginNr);

    Optional<FoodCourt> getById(UUID id);

    Optional<Image> getImageByUri(URI uri);

    Optional<Image> getImageByID(UUID id);

    void persist(FoodCourt foodCourt);

    void delete(FoodCourt foodCourt);

    List<FoodCourt> listAll();

    void persistImage(Image image);

    void deleteImage(Image image);
}
