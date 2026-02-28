package com.ffb.app.service.impl.notification;
import com.ffb.app.dao.api.notification.NotificationDao;
import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.app.service.api.notification.NotificationService;
import com.ffb.model.api.response.notification.FoodOrderNotificationResponse;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.db.object.notification.FoodOrderNotification;
import com.ffb.model.db.object.notification.NotificationStatus;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {

    private final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);


    private final NotificationDao notificationDao;
    private final ResponseMapper mapper;

    @Inject
    public NotificationServiceImpl(NotificationDao notificationDao, ResponseMapper mapper) {
        this.notificationDao = notificationDao;
        this.mapper = mapper;
    }

    @Override
    public List<FoodOrderNotificationResponse> listByLoginNrAndAccountType(String loginNr, AccountType accountType) throws ServiceException {
        List<FoodOrderNotification> notifications;
        if (accountType == AccountType.ADMIN) {
            LOG.info("Listing all Food Orders (ADMIN)");
            notifications = notificationDao.listAll();
        } else if (accountType == AccountType.GUEST) {
            LOG.info("Listing all Food Orders (GUEST)");
            notifications = notificationDao.listByLoginNr(loginNr);

        } else {
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        return notifications.stream().map(mapper::getFoodOrderNotificationResponse).toList();
    }

    @Override
    @Transactional
    public FoodOrderNotificationResponse setStatusById(UUID notificationId, NotificationStatus newStatus) throws ServiceException {
        FoodOrderNotification notification;
        try {
            notification = notificationDao.getById(notificationId);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        if (newStatus == notification.getStatus()) {
            return mapper.getFoodOrderNotificationResponse(notification);
        }
        notification.setStatus(newStatus);
        return mapper.getFoodOrderNotificationResponse(notification);
    }
}
