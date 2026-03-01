package com.ffb.app.service.api.notification;

import com.ffb.model.api.response.notification.FoodOrderNotificationResponse;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.db.object.notification.NotificationStatus;
import com.ffb.model.exception.ServiceException;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<FoodOrderNotificationResponse> listByLoginNrAndAccountType(String loginNr, AccountType accountType) throws ServiceException;

    FoodOrderNotificationResponse setStatusById(UUID notificationId, NotificationStatus newStatus) throws ServiceException;
}
