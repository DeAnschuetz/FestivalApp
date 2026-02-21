package com.ffb.app.dao.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import com.ffb.model.exception.DaoException;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface FoodCourtDao {

    FoodCourt getByLoginNr(String loginNr) throws DaoException;

    FoodCourt getById(UUID id) throws DaoException;

    Image getImageByUri(URI uri) throws DaoException;

    Image getImageByID(UUID id) throws DaoException;

    void persist(FoodCourt foodCourt);

    void delete(FoodCourt foodCourt);

    List<FoodCourt> listAll();

    void persistImage(Image image);

    void deleteImage(Image image);
}
