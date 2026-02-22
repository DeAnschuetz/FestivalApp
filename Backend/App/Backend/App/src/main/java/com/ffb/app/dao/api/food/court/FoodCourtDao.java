package com.ffb.app.dao.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import com.ffb.model.exception.DaoException;

import java.io.PushbackInputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface FoodCourtDao {

    FoodCourt getByLoginNr(String loginNr) throws DaoException;

    FoodCourt getById(UUID id) throws DaoException;

    byte[] getImageById(UUID id) throws DaoException;

    void persist(FoodCourt foodCourt);

    void delete(FoodCourt foodCourt);

    List<FoodCourt> listAll();

    void addImage(String loginNr, PushbackInputStream image) throws DaoException;
}
