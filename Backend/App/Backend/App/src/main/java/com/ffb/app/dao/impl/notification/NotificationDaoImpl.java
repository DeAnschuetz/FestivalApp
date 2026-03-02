package com.ffb.app.dao.impl.notification;

import com.ffb.app.dao.api.notification.NotificationDao;
import com.ffb.app.repository.api.notification.NotificationRepository;
import com.ffb.model.db.object.notification.FoodOrderNotification;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NotificationDaoImpl implements NotificationDao {

    private final NotificationRepository repository;

    public NotificationDaoImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FoodOrderNotification> listAll() {
        return repository.listAll();
    }

    @Override
    public List<FoodOrderNotification> listByLoginNr(String loginNr) {
        return repository.listAllByLoginNr(loginNr);
    }

    @Override
    public FoodOrderNotification getById(UUID notificationId) throws DaoException {
        FoodOrderNotification notification = repository.findById(notificationId);
        if (notification == null) {
            throw new DaoException("Notification with id " + notificationId + " not found");
        }
        return notification;
    }
}
