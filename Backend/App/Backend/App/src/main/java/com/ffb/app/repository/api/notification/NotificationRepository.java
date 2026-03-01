package com.ffb.app.repository.api.notification;

import com.ffb.model.db.object.notification.FoodOrderNotification;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends PanacheRepositoryBase<FoodOrderNotification, UUID> {

    List<FoodOrderNotification> listAllByLoginNr(String loginNr);
}
