package com.ffb.app.service.api.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import com.ffb.model.exception.ServiceException;
import jakarta.persistence.EntityNotFoundException;

import java.io.PushbackInputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface FoodCourtService {

    List<FoodCourt> listAll();

    FoodCourt getById(UUID id) throws ServiceException;

    FoodCourt getByLoginNr(String loginNr) throws ServiceException;

    FoodCourt create(String LoginNr, String name) throws ServiceException;

    FoodCourt updateByLoginNr(String loginNr, String name) throws ServiceException;

    FoodCourt updateById(UUID id, String loginNr, String name) throws ServiceException;

    void delete(UUID id) throws ServiceException;

    FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders) throws ServiceException;

    Image getImageByUri(URI uri) throws ServiceException;

    Image getImageByID(UUID id) throws ServiceException;

    URI addImage(String loginNr, PushbackInputStream inputData) throws ServiceException;

    byte[] getImageByFoodCourtId(UUID foodCourtId);
}
