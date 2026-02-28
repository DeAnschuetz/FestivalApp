package com.ffb.app.service.impl.food.court;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.service.api.food.court.FoodCourtService;
import com.ffb.model.api.request.food.court.FoodCourtRequest;
import com.ffb.model.api.request.food.court.FoodCourtRequestFull;
import com.ffb.model.api.request.food.court.FoodCourtRequestSimple;
import com.ffb.model.api.request.food.court.FoodCourtWithRelationsRequest;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.io.PushbackInputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtServiceImpl implements FoodCourtService {

    // TODO Logging
    private static final Logger LOG = Logger.getLogger(FoodCourtServiceImpl.class);

    private final AccountDao accountDao;
    private final FoodCourtDao foodCourtDao;

    @Inject
    public FoodCourtServiceImpl(FoodCourtDao foodCourtDao, AccountDao accountDao) {
        this.foodCourtDao = foodCourtDao;
        this.accountDao = accountDao;
    }

    @Override
    public FoodCourtResponse get(UUID id) throws ServiceException {
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        return getFoodCourtResponse(foodCourt);
    }

    @Override
    public FoodCourtResponse get(String loginNr) throws ServiceException {
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        return getFoodCourtResponse(foodCourt);
    }

    @Override
    public FoodCourtResponse get(FoodCourtWithRelationsRequest request) throws ServiceException  {
        UUID foodCourtId = request.foodCourtId();
        boolean foodOrders = request.foodOrders();
        boolean waitingTime = request.waitingTime();
        FoodCourt foodCourt;
        if (waitingTime && foodOrders) {
            try {
                foodCourt = foodCourtDao.getByIdWithWaitingTimeAndFoodOrders(foodCourtId);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else if(waitingTime) {
            try {
                foodCourt = foodCourtDao.getByIdWithWaitingTime(foodCourtId);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else if(foodOrders) {
            try {
                foodCourt = foodCourtDao.getByIdWithFoodOrders(foodCourtId);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else {
            try {
                foodCourt = foodCourtDao.getById(foodCourtId);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        }
        return getFoodCourtResponse(foodCourt);
    }

    @Override
    @Transactional
    public byte[] getImage(UUID foodCourtId) throws ServiceException {
        try {
            return foodCourtDao.getImageById(foodCourtId);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    public List<FoodCourtResponse> listAll() {
        return foodCourtDao.listAll()//
                .stream()//
                .map(this::getFoodCourtResponse)//
                .toList()//
        ;
    }

    @Override
    @Transactional
    public FoodCourtResponse create(UUID id, FoodCourtRequest request) throws ServiceException {
        String name = request.displayName();
        String loginNr = request.loginNr();
        return createFoodCourt(name, loginNr);
    }

    @Override
    @Transactional
    public FoodCourtResponse create(String loginNr, FoodCourtRequestSimple request) throws ServiceException {
        String name = request.displayName();
        return createFoodCourt(name, loginNr);
    }

    @Override
    @Transactional
    public FoodCourtResponse update(UUID id, FoodCourtRequest request) throws ServiceException {
        String name = request.displayName();
        String loginNr = request.loginNr();
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodCourt.setDisplayName(name);
        foodCourtDao.persist(foodCourt);
        return getFoodCourtResponse(foodCourt);
    }

    @Override
    @Transactional
    public FoodCourtResponse update(String loginNr, FoodCourtRequestSimple request) throws ServiceException {
        String name = request.displayName();
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodCourt.setDisplayName(name);
        foodCourtDao.persist(foodCourt);
        return getFoodCourtResponse(foodCourt);
    }

    @Override
    @Transactional
    public void delete(UUID id) throws ServiceException {
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
    public void addImage(UUID id, PushbackInputStream inputData) throws ServiceException {
        LOG.info("Adding image for food court with id: " + id);
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Account account = foodCourt.getAccount();
        String loginNr = account.getLoginNr();

        try {
            foodCourtDao.addImage(loginNr, inputData);
            LOG.info("Added image for food court with loginNr: " + loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void createFoodCourts(List<FoodCourtRequestFull> foodCourtRequests) {
        foodCourtRequests.stream()//
                .map(request -> {
                    try {
                        return createFoodCourt(request.id(), request.loginNr(), request.name());
                    } catch (ServiceException e) {
                        LOG.error("could no create foodCourt for request={{}}; Exception: ", request, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList()
        ;
    }

    /*
		Private Helper Functions
	*/

    private FoodCourtResponse createFoodCourt(UUID id, String loginNr, String name) throws ServiceException {
        Account account;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourt foodCourt = null;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            // DO Nothing
        }
        if (foodCourt != null) {
            throw new ServiceException("The FoodCourt already Exists", Response.Status.CONFLICT);
        }
        foodCourt = new FoodCourt(
                id,
                name
        );
        foodCourt.setAccount(account);
        foodCourtDao.persist(foodCourt);
        return getFoodCourtResponse(foodCourt);
    }

    private FoodCourtResponse createFoodCourt(String name, String loginNr) throws ServiceException {
        Account account;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourt foodCourt = null;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            // DO Nothing
        }
        if (foodCourt != null) {
            throw new ServiceException("The FoodCourt already Exists", Response.Status.CONFLICT);
        }
        foodCourt = new FoodCourt(
                name
        );
        foodCourt.setAccount(account);
        foodCourtDao.persist(foodCourt);
        return getFoodCourtResponse(foodCourt);
    }

    private FoodCourtResponse getFoodCourtResponse(FoodCourt foodCourt) {
        return new FoodCourtResponse(foodCourt.getId(), foodCourt.getDisplayName(), foodCourt.getWaitingTime());
    }
}
