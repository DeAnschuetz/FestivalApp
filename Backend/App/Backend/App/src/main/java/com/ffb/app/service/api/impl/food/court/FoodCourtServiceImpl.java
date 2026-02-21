package com.ffb.app.service.api.impl.food.court;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.repository.api.file.FileDao;
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
    private final FileDao fileDao;

    @Inject
    public FoodCourtServiceImpl(FoodCourtDao foodCourtDao, AccountDao accountDao, FileDao fileDao) {
        this.foodCourtDao = foodCourtDao;
        this.accountDao = accountDao;
        this.fileDao = fileDao;
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
        Account account = null;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
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
        FoodCourt foodCourt = null;
        try {
            foodCourt = getByLoginNr(loginNr);
        } catch (ServiceException e) {
            throw new ServiceException(e);
        }
        foodCourt.setDisplayName(name);
        foodCourtDao.persist(foodCourt);
        return foodCourt;
    }

    @Override
    @Transactional
    public FoodCourt updateById(UUID id, String loginNr, String name) throws EntityNotFoundException, ServiceException {
        Account account = null;
        try {
            account = accountDao.getByLoginNr(loginNr);
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
        FoodCourt foodCourt = null;
        try {
            foodCourt = getById(id);
        } catch (ServiceException e) {
            throw new ServiceException(e);
        }
        foodCourtDao.delete(foodCourt);
    }

    /**
     * Optional: force-load relations for serialization/use in resource layer.
     * Only do this inside a transaction.
     */
    @Override
    @Transactional
    public FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders) throws ServiceException  {
        try {
            return getById(id);
        } catch (ServiceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Image getImageByUri(URI uri) throws ServiceException {
        try {
            return foodCourtDao.getImageByUri(uri);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Image getImageByID(UUID id) throws ServiceException {
        try {
            return foodCourtDao.getImageByID(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public URI addImage(String loginNr, PushbackInputStream inputData) throws ServiceException {
        LOG.info("Adding image for food court with loginNr: " + loginNr);
        UUID imageId = UUID.randomUUID();
        URI fileUri = fileDao.createNewImage(imageId, inputData);
        Image image = new Image(imageId, fileUri);
        FoodCourt foodCourt = null;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        if(foodCourt.getImage() != null) {
            Image oldImage = foodCourt.getImage();
            foodCourtDao.deleteImage(oldImage);
        }
        foodCourt.setImage(image);
        LOG.info("Added image for food court with loginNr: " + loginNr);
        foodCourtDao.persistImage(image);
        foodCourtDao.persist(foodCourt);
        return fileUri;
    }

    @Override
    public byte[] getImageByFoodCourtId(UUID foodCourtId) {
        return new byte[0];
    }
}
