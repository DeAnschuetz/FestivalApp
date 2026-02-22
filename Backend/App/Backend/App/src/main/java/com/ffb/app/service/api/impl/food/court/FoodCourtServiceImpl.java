package com.ffb.app.service.api.impl.food.court;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.service.api.api.food.court.FoodCourtService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import java.io.PushbackInputStream;
import java.net.URI;
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
            throw new ServiceException(e);
        }
    }

    public FoodCourt getByLoginNr(String loginNr) throws ServiceException {
        try {
            return foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<FoodCourt> listAll() {
        return foodCourtDao.listAll();
    }

    @Override
    @Transactional
    public FoodCourt create(String loginNr, String name) throws ServiceException {
        try {
            Account account = accountDao.findByLoginNr(loginNr);
            FoodCourt foodCourt = new FoodCourt(
                    name
            );
            foodCourt.setAccount(account);
            foodCourtDao.persist(foodCourt);
            return foodCourt;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public FoodCourt updateByLoginNr(String loginNr, String name) throws ServiceException {
        try {
            FoodCourt foodCourt = foodCourtDao.getByLoginNr(loginNr);
            foodCourt.setDisplayName(name);
            foodCourtDao.persist(foodCourt);
            return foodCourt;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public FoodCourt updateById(UUID id, String loginNr, String name) throws EntityNotFoundException, ServiceException {
        try {
            Account account = accountDao.getByLoginNr(loginNr);
            FoodCourt foodCourt = getById(id);
            foodCourt.setAccount(account);
            foodCourt.setDisplayName(name);
            foodCourtDao.persist(foodCourt);
            return foodCourt;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) throws EntityNotFoundException, ServiceException {
        try {
            FoodCourt foodCourt = foodCourtDao.getById(id);
            foodCourtDao.delete(foodCourt);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders) throws ServiceException  {
        try {
            return foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
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
            throw new ServiceException(e);
        }
    }

    @Override
    public byte[] getImageByFoodCourtId(UUID foodCourtId) throws ServiceException {
        try {
            return foodCourtDao.getImageById(foodCourtId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
