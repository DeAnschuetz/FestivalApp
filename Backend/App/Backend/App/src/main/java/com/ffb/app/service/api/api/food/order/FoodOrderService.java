package com.ffb.app.service.api.api.food.order;

import com.ffb.model.api.response.order.FoodOrderResponse;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
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
    void shareOrder(String loginNr, UUID orderId, String sharedLoginNr) throws ServiceException;

    FoodOrderResponse getById(UUID id, boolean withItems, boolean withHistory) throws ServiceException;

    List<FoodOrderResponse> listAll(boolean withItems);

    List<FoodOrderResponse> listByLoginNrAndAccountType(String loginNr, AccountType accountType, boolean withItems) throws ServiceException;

    List<FoodOrderResponse> listByLoginNrAndAccountTypeAndStatus(String loginNr, AccountType accountType, FoodOrderStatus status, boolean withItems) throws ServiceException;
}
