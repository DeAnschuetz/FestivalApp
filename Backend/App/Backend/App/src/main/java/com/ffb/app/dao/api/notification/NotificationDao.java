package com.ffb.app.dao.api.notification;

import com.ffb.model.db.object.notification.FoodOrderNotification;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.UUID;

public interface NotificationDao {

    List<FoodOrderNotification> listAll();

    List<FoodOrderNotification> listByLoginNr(String loginNr);

    FoodOrderNotification getById(UUID notificationId) throws DaoException;
}
