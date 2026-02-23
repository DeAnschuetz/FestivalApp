package com.ffb.app.service.api.impl.food.court;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.service.api.api.food.court.FoodCourtService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtServiceImpl implements FoodCourtService {

    private static final Logger LOG = Logger.getLogger(FoodCourtServiceImpl.class);

    private final AccountDao accountDao;
    private final FoodCourtDao foodCourtDao;

    @Inject
    public FoodCourtServiceImpl(FoodCourtDao foodCourtDao, AccountDao accountDao) {
        this.foodCourtDao = foodCourtDao;
        this.accountDao = accountDao;
    }

    @Override
    public FoodCourt getById(UUID id) throws ServiceException {
        try {
            return foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    public FoodCourt getByLoginNr(String loginNr) throws ServiceException {
        try {
            return foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    public List<FoodCourt> listAll() {
        return foodCourtDao.listAll();
    }

    @Override
    @Transactional
    public FoodCourt create(String loginNr, String name) throws ServiceException {
        Account account;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourt foodCourt = new FoodCourt(
                name
        );
        foodCourt.setAccount(account);
        foodCourtDao.persist(foodCourt);
        return foodCourt;
    }

    @Override
    public FoodCourt updateByLoginNr(String loginNr, String name) throws ServiceException {
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodCourt.setDisplayName(name);
        foodCourtDao.persist(foodCourt);
        return foodCourt;
    }

    @Override
    @Transactional
    public FoodCourt updateById(UUID id, String loginNr, String name) throws EntityNotFoundException, ServiceException {
        Account account;
        FoodCourt foodCourt;
        try {
            account = accountDao.getByLoginNr(loginNr);
            foodCourt = getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodCourt.setAccount(account);
        foodCourt.setDisplayName(name);
        foodCourtDao.persist(foodCourt);
        return foodCourt;
    }

    @Override
    @Transactional
    public void delete(UUID id) throws EntityNotFoundException, ServiceException {
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodCourtDao.delete(foodCourt);
    }

    @Override
    @Transactional
    public FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders) throws ServiceException  {
        try {
            return foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void addImage(String loginNr, PushbackInputStream inputData) throws ServiceException {
        LOG.info("Adding image for food court with loginNr: " + loginNr);
        try {
            foodCourtDao.addImage(loginNr, inputData);
            LOG.info("Added image for food court with loginNr: " + loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public byte[] getImageByFoodCourtId(UUID foodCourtId) throws ServiceException {
        try {
            return foodCourtDao.getImageById(foodCourtId);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }
}
