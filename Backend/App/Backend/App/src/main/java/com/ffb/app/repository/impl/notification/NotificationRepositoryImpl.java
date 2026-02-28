package com.ffb.app.repository.impl.notification;

import com.ffb.app.repository.api.notification.NotificationRepository;
import com.ffb.model.db.object.notification.FoodOrderNotification;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class NotificationRepositoryImpl implements NotificationRepository {

    @Override
    public List<FoodOrderNotification> listAllByLoginNr(String loginNr) {
        return find(
                    "SELECT n " +
                    "FROM FoodOrderNotification n " +
                    "WHERE n.account.ticket.loginNr = ?1",
                    loginNr
                )//
                .list()//
        ;
    }
}
