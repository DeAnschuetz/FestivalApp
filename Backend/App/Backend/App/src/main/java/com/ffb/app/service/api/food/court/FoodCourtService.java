package com.ffb.app.service.api.food.court;

import com.ffb.model.api.request.food.court.FoodCourtRequest;
import com.ffb.model.api.request.food.court.FoodCourtRequestFull;
import com.ffb.model.api.request.food.court.FoodCourtRequestSimple;
import com.ffb.model.api.request.food.court.FoodCourtWithRelationsRequest;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.exception.ServiceException;

import jakarta.transaction.Transactional;

import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;

public interface FoodCourtService {

    FoodCourtResponse get(UUID id) throws ServiceException;

    FoodCourtResponse get(String loginNr) throws ServiceException;

    @Transactional
    byte[] getImage(UUID foodCourtId) throws ServiceException;

    List<FoodCourtResponse> listAll();

    @Transactional
    FoodCourtResponse create(String loginNr, FoodCourtRequestSimple request) throws ServiceException;

    @Transactional
    FoodCourtResponse create(UUID id, FoodCourtRequest name) throws ServiceException;

    @Transactional
    FoodCourtResponse update(UUID id, FoodCourtRequest name) throws ServiceException;

    @Transactional
    FoodCourtResponse update(String loginNr, FoodCourtRequestSimple request) throws ServiceException;

    @Transactional
    void delete(UUID id) throws ServiceException;

    @Transactional
    void addImage(String loginNr, PushbackInputStream inputData) throws ServiceException;

    @Transactional
    void addImage(UUID id, PushbackInputStream inputData) throws ServiceException;

    @Transactional
    void createFoodCourts(List<FoodCourtRequestFull> foodCourtRequests);
}
