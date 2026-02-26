package com.ffb.app.service.api.api.food.court;

import com.ffb.model.api.response.food_court.FoodCourtResponse;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.exception.ServiceException;
import jakarta.transaction.Transactional;

import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;

public interface FoodCourtService {

    FoodCourtResponse get(UUID id) throws ServiceException;

    FoodCourtResponse get(String loginNr) throws ServiceException;

    FoodCourtResponse get(UUID id, boolean waitingTime, boolean foodOrders) throws ServiceException;

    @Transactional
    byte[] getImage(UUID foodCourtId) throws ServiceException;

    List<FoodCourtResponse> listAll();

    @Transactional
    FoodCourtResponse create(String LoginNr, String name) throws ServiceException;

    @Transactional
    FoodCourtResponse create(UUID id, String loginNr, String name) throws ServiceException;

    @Transactional
    FoodCourtResponse update(String loginNr, String name) throws ServiceException;

    @Transactional
    FoodCourtResponse update(UUID id, String loginNr, String name) throws ServiceException;

    @Transactional
    void delete(UUID id) throws ServiceException;

    @Transactional
    void addImage(String loginNr, PushbackInputStream inputData) throws ServiceException;

    @Transactional
    void addImage(UUID id, PushbackInputStream inputData) throws ServiceException;
}
