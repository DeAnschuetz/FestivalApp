package com.ffb.app.service.api.food.order;

import com.ffb.model.api.request.food.order.ShareOrderRequest;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponseFull;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.exception.ServiceException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface FoodOrderService {

    @Transactional
    List<FoodOrderResponse> create(String loginNr) throws ServiceException;

    @Transactional
    FoodOrderResponse updateStatus(UUID orderId, FoodOrderStatus newStatus) throws ServiceException;

    @Transactional
    void delete(UUID id) throws ServiceException;

    @Transactional
    void shareOrder(String loginNr, ShareOrderRequest request) throws ServiceException;

    FoodOrderResponseFull getById(UUID id, boolean withItems, boolean withHistory) throws ServiceException;

    List<FoodOrderResponse> listAll(boolean withItems);

    List<FoodOrderResponse> listByLoginNrAndAccountType(String loginNr, AccountType accountType, boolean withItems) throws ServiceException;

    List<FoodOrderResponse> listByLoginNrAndAccountTypeAndStatus(String loginNr, AccountType accountType, FoodOrderStatus status, boolean withItems) throws ServiceException;
}
