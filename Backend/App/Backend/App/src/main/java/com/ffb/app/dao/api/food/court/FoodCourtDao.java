package com.ffb.app.dao.api.food.court;

import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.exception.DaoException;

import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;

public interface FoodCourtDao {

    FoodCourt getById(UUID id) throws DaoException;

    FoodCourt getByLoginNr(String loginNr) throws DaoException;

    byte[] getImageById(UUID id) throws DaoException;

    List<FoodCourt> listAll();

    void addImage(String loginNr, PushbackInputStream image) throws DaoException;

    void persist(FoodCourt foodCourt);

    void delete(FoodCourt foodCourt);
}
