package com.ffb.app.service.impl.food.court;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.app.service.api.food.court.FoodCourtService;
import com.ffb.model.api.request.food.court.FoodCourtRequest;
import com.ffb.model.api.request.food.court.FoodCourtRequestFull;
import com.ffb.model.api.request.food.court.FoodCourtRequestSimple;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import org.jspecify.annotations.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PushbackInputStream;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtServiceImpl implements FoodCourtService {

    // TODO Logging Example
    private static final Logger LOG = LoggerFactory.getLogger(FoodCourtServiceImpl.class);

    private final AccountDao accountDao;
    private final FoodCourtDao foodCourtDao;
    private final ResponseMapper mapper;

    @Inject
    public FoodCourtServiceImpl(FoodCourtDao foodCourtDao, AccountDao accountDao, ResponseMapper mapper) {
        this.foodCourtDao = foodCourtDao;
        this.accountDao = accountDao;
        this.mapper = mapper;
    }

    @Override
    public FoodCourtResponse get(@NonNull UUID id) throws ServiceException {
        LOG.trace("ENTER: get; id={{}}", id);
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            LOG.error("could not get foodCourt by id={{}}; Exception: ", id, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourtResponse response = mapper.getFoodCourtResponse(foodCourt);
        LOG.trace("EXIT: get; id={{}}, response=[{}]", id, response);
        return response;
    }

    @Override
    public FoodCourtResponse get(@NonNull String loginNr) throws ServiceException {
        LOG.trace("ENTER: get; loginNr={{}}", loginNr);
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not get foodCourt by loginNr={{}}; Exception: ", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourtResponse response = mapper.getFoodCourtResponse(foodCourt);
        LOG.trace("EXIT: get; loginNr={{}}, response={}", loginNr, response);
        return response;
    }

    @Override
    @Transactional
    public byte[] getImage(@NonNull UUID foodCourtId) throws ServiceException {
        LOG.trace("ENTER: getImage; foodCourtId={{}}", foodCourtId);
        try {
            byte[] img = foodCourtDao.getImageById(foodCourtId);
            LOG.trace("EXIT: getImage; foodCourtId={{}}, bytes={}", foodCourtId, (img == null ? 0 : img.length));
            return img;
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    public List<FoodCourtResponse> listAll() {
        LOG.trace("ENTER: listAll");
        List<FoodCourtResponse> list = foodCourtDao.listAll().stream()//
                .map(mapper::getFoodCourtResponse)//
                .toList()//
        ;
        LOG.trace("EXIT: listAll found {} foodCourts", list.size());
        return list;
    }

    @Override
    @Transactional
    public FoodCourtResponse create(@NonNull UUID id, @NonNull FoodCourtRequest request) throws ServiceException {
        LOG.trace("ENTER: create; id={{}}, request=[{}]", id, request);
        String name = request.displayName();
        String loginNr = request.loginNr();
        FoodCourtResponse response = createFoodCourt(name, loginNr);
        LOG.trace("EXIT: create; id={{}}, loginNr={{}}, response=[{}]", id, loginNr, response);
        return response;
    }

    @Override
    @Transactional
    public FoodCourtResponse create(@NonNull String loginNr, @NonNull FoodCourtRequestSimple request) throws ServiceException {
        LOG.trace("ENTER: create; loginNr={{}}, request=[{}]", loginNr, request);
        String name = request.displayName();
        FoodCourtResponse response = createFoodCourt(name, loginNr);
        LOG.trace("EXIT: create; loginNr={{}}, response=[{}]", loginNr, response);
        return response;
    }

    @Override
    @Transactional
    public FoodCourtResponse update(@NonNull UUID id, @NonNull FoodCourtRequest request) throws ServiceException {
        LOG.trace("ENTER: update; id={{}}, request=[{}]", id, request);
        String name = request.displayName();
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            LOG.error("could not get foodCourt for update by id={{}}; Exception: ", id, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        foodCourt.setDisplayName(name);
        FoodCourtResponse response = mapper.getFoodCourtResponse(foodCourt);
        LOG.info("updated foodCourt displayName; id={{}}, newName={}", id, name);
        LOG.trace("EXIT: update; id={{}}, response=[{}]", id, response);
        return response;
    }

    @Override
    @Transactional
    public FoodCourtResponse update(@NonNull String loginNr, @NonNull FoodCourtRequestSimple request) throws ServiceException {
        LOG.trace("ENTER: update; loginNr={{}}, request=[{}]", loginNr, request);
        String name = request.displayName();
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not get foodCourt for update by loginNr={{}}; Exception: ", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        foodCourt.setDisplayName(name);
        FoodCourtResponse response = mapper.getFoodCourtResponse(foodCourt);
        LOG.info("updated foodCourt displayName; loginNr={{}}, newName={}", loginNr, name);
        LOG.trace("EXIT: update; loginNr={{}}, response=[{}]", loginNr, response);
        return response;
    }

    @Override
    @Transactional
    public void delete(@NonNull UUID id) throws ServiceException {
        LOG.trace("ENTER: delete; id={{}}", id);
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            LOG.error("could not get foodCourt for delete by id={{}}; Exception: ", id, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodCourtDao.delete(foodCourt);
        LOG.info("deleted foodCourt; id={{}}", id);
        LOG.trace("EXIT: delete; id={{}}", id);
    }

    @Override
    @Transactional
    public void addImage(@NonNull String loginNr, PushbackInputStream inputData) throws ServiceException {
        LOG.trace("ENTER: addImage; loginNr={{}}", loginNr);
        try {
            foodCourtDao.addImage(loginNr, inputData);
        } catch (DaoException e) {
            LOG.error("could not add image for loginNr={{}}; Exception: ", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        LOG.info("added image for foodCourt; loginNr={{}}", loginNr);
        LOG.trace("EXIT: addImage; loginNr={{}}", loginNr);
    }

    @Override
    @Transactional
    public void addImage(@NonNull UUID id, PushbackInputStream inputData) throws ServiceException {
        LOG.trace("ENTER: addImage; id={{}}", id);
        FoodCourt foodCourt;
        try {
            foodCourt = foodCourtDao.getById(id);
        } catch (DaoException e) {
            LOG.error("could not get foodCourt by id={{}} to add image; Exception: ", id, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Account account = foodCourt.getAccount();
        String loginNr = account.getLoginNr();
        LOG.debug("resolved loginNr={{}} from foodCourtId={{}}", loginNr, id);

        try {
            foodCourtDao.addImage(loginNr, inputData);
        } catch (DaoException e) {
            LOG.error("could not add image for foodCourtId={{}}, loginNr={{}}; Exception: ", id, loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        LOG.info("added image for foodCourt; foodCourtId={{}}, loginNr={{}}", id, loginNr);
        LOG.trace("EXIT: addImage; id={{}}", id);
    }

    @Override
    @Transactional
    public void createFoodCourts(@NonNull List<FoodCourtRequestFull> foodCourtRequests) {
        LOG.trace("ENTER: createFoodCourts; requests=[{}]", foodCourtRequests);

        List<FoodCourtResponse> created = foodCourtRequests.stream()
                .map(request -> {
                    try {
                        LOG.debug("creating foodCourt from request=[{}]", request);
                        return createFoodCourt(request.id(), request.loginNr(), request.name());
                    } catch (ServiceException e) {
                        LOG.error("could not create foodCourt for request=[{}]; Exception:", request, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        LOG.trace("EXIT: createFoodCourts created {} foodCourts", created.size());
    }

    /*
		Private Helper Functions
	*/

    private FoodCourtResponse createFoodCourt(@NonNull UUID id, @NonNull String loginNr, @NonNull String name) throws ServiceException {
        LOG.trace("ENTER: createFoodCourt; id={{}}, loginNr={{}}, name={}", id, loginNr, name);
        Account account;
        try {
            account = accountDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not get account by loginNr={{}}for foodCourt creation; Exception: ", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourt existing = null;
        try {
            existing = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.debug("no existing foodCourt found for loginNr={{}} (ok); Exception: ", loginNr, e);
        }
        if (existing != null) {
            LOG.error("foodCourt already exists; loginNr={{}}, existingId={{}}", loginNr, existing.getId());
            throw new ServiceException("The FoodCourt already Exists", Response.Status.CONFLICT);
        }
        FoodCourt foodCourt = new FoodCourt(
                id,
                name,
                account
        );
        foodCourt.setAccount(account);
        foodCourtDao.persist(foodCourt);
        LOG.info("foodCourt created; id={{}}, loginNr={{}}, name={}", id, loginNr, name);
        FoodCourtResponse response = mapper.getFoodCourtResponse(foodCourt);
        LOG.trace("EXIT: createFoodCourt; response=[{}]", response);
        return response;
    }

    private FoodCourtResponse createFoodCourt(@NonNull String name, @NonNull String loginNr) throws ServiceException {
        LOG.trace("ENTER: createFoodCourt; loginNr={{}}, name={}", loginNr, name);
        Account account;
        try {
            account = accountDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not get account by loginNr={{}} for foodCourt creation; Exception:", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        FoodCourt existing = null;
        try {
            existing = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.debug("no existing foodCourt found for loginNr={{}} (ok); Exception:", loginNr, e);
        }
        if (existing != null) {
            LOG.error("foodCourt already exists; loginNr={{}}, existingId={{}}", loginNr, existing.getId());
            throw new ServiceException("The FoodCourt already Exists", Response.Status.CONFLICT);
        }
        FoodCourt foodCourt = new FoodCourt(
                name,
                account
        );
        foodCourt.setAccount(account);
        foodCourtDao.persist(foodCourt);

        LOG.info("foodCourt created; loginNr={{}}, name={}", loginNr, name);
        FoodCourtResponse response = mapper.getFoodCourtResponse(foodCourt);
        LOG.trace("EXIT: createFoodCourt; response=[{}]", response);
        return response;
    }
}
