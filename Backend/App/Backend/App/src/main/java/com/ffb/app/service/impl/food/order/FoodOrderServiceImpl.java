package com.ffb.app.service.impl.food.order;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.app.service.api.food.order.FoodOrderService;
import com.ffb.model.api.request.food.order.ShareOrderRequest;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponseFull;
import com.ffb.model.api.response.food.order.FoodOrderResponseHistory;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.db.object.cart.CartItem;
import com.ffb.model.db.object.credit.Credit;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderItem;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.db.object.product.Product;
import com.ffb.model.exception.CustomRuntimeException;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FoodOrderServiceImpl implements FoodOrderService {

    // TODO Logging fertig
    private static final Logger LOG = LoggerFactory.getLogger(FoodOrderService.class);

    @ConfigProperty(name = "order.extra.price")
    double EXTRA_PRICE;

    private final FoodOrderDao foodOrderDao;
    private final AccountDao accountDao;
    private final CartDao cartDao;
    private final FoodCourtDao foodCourtDao;
    private final CreditDao creditDao;
    private final ResponseMapper responseMapper;

    @Inject
    public FoodOrderServiceImpl(
            FoodOrderDao foodOrderDao,
            AccountDao accountDao,
            CartDao cartDao,
            FoodCourtDao foodCourtDao,
            CreditDao creditDao, ResponseMapper responseMapper
    ) {
        this.foodOrderDao = foodOrderDao;
        this.accountDao = accountDao;
        this.cartDao = cartDao;
        this.foodCourtDao = foodCourtDao;
        this.creditDao = creditDao;
        this.responseMapper = responseMapper;
    }

    @Transactional
    @Override
    public List<FoodOrderResponse> create(String loginNr) throws ServiceException {
        LOG.trace("ENTER: create; loginNr={{}}", loginNr);
        try {
            Account account;
            try {
                account = accountDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                LOG.error("could not find account for loginNr={{}}; Exception:", loginNr, e);
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            Cart cart;
            try {
                cart = cartDao.findByLoginNr(loginNr);
            } catch (DaoException e) {
                LOG.error("could not find cart for loginNr={{}}; Exception:", loginNr, e);
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }

            List<CartItem> cartItems = cart.getCartItems();
            LOG.debug("creating food orders from cart; loginNr={{}}, cartItems=[{}], hasPrio={}", loginNr, cartItems, cart.isHasPrio());

            List<FoodOrderResponse> created = cartItems.stream()//
                    .collect(
                            Collectors.groupingBy(
                                    item -> item.getProduct().getFoodCourt().getId(),
                                    Collectors.mapping(item -> {
                                                Product currentProduct = item.getProduct();
                                                String extra = item.getExtra();

                                                double price = extra.isBlank()
                                                        ? currentProduct.getPrice()
                                                        : currentProduct.getPrice() + EXTRA_PRICE;

                                                return new FoodOrderItem(
                                                        UUID.randomUUID(),
                                                        price,
                                                        item.getItemCount(),
                                                        extra,
                                                        item.getProduct()
                                                );
                                            },
                                            Collectors.toList()
                                    )//
                            )//
                    )//
                    .entrySet().stream()//
                    .map(entry -> {
                            UUID foodCourtId = entry.getKey();
                            List<FoodOrderItem> foodOrderItems = entry.getValue();
                            double total = foodOrderItems.stream()//
                                    .mapToDouble(item -> item.getPrice() * item.getItemCount())//
                                    .sum()//
                            ;
                            LOG.debug("creating FoodOrder; loginNr={{}}, foodCourtId={{}}, items=[{}], total={}", loginNr, foodCourtId, foodOrderItems, total);

                            FoodCourt foodCourt;
                            try {
                                foodCourt = foodCourtDao.getById(foodCourtId);
                            } catch (DaoException e) {
                                LOG.error("could not find foodCourt for id={{}}; loginNr={{}} Exception:", foodCourtId, loginNr, e);
                                throw new CustomRuntimeException(e, Response.Status.NOT_FOUND);
                            }
                            FoodOrder foodOrder = new FoodOrder(
                                    cart.isHasPrio(),
                                    total,
                                    foodOrderItems,
                                    foodCourt,
                                    account
                            );
                            foodOrderItems.forEach(foodOrderItem -> {
                                foodOrderItem.setOrder(foodOrder);
                            });

                            Credit currentCredit;
                            try {
                                currentCredit = creditDao.getByLoginNr(loginNr);
                            } catch (DaoException e) {
                                LOG.error("could not find foodCourt for id={{}}; loginNr={{}} Exception:", foodCourtId, loginNr, e);
                                throw new CustomRuntimeException(e, Response.Status.NOT_FOUND);
                            }
                            double currentCreditAmount = currentCredit.getAmount();
                            if (currentCreditAmount < total) {
                                LOG.warn("insufficient credit; loginNr={{}}, currentCredit={}, requiredTotal={}", loginNr, currentCreditAmount, total);
                                throw new CustomRuntimeException("Insufficient credit", Response.Status.BAD_REQUEST);
                            }
                            currentCredit.setAmount(currentCreditAmount - total);

                            LOG.info("persisting foodOrder; loginNr={{}}, foodCourtId={{}}, total={}, prio={}", loginNr, foodCourtId, total, cart.isHasPrio());
                            foodOrderDao.persist(foodOrder);

                            LOG.debug("emptying cart; loginNr={{}}", loginNr);
                            cartDao.empty(cart);
                            return foodOrder;
                        }//
                    )//
                    .map(responseMapper::getFoodOrderResponse)
                    .toList()//
            ;
            LOG.trace("EXIT: create; loginNr={{}} created {} orders", loginNr, created.size());
            return created;
        } catch (CustomRuntimeException e) {
            LOG.error("unexpected error in create; loginNr={{}}; Exception:", loginNr, e);
            throw new ServiceException(e);
        }

    }

    @Transactional
    @Override
    public FoodOrderResponse updateStatus(UUID orderId, FoodOrderStatus newStatus) throws ServiceException {
        LOG.trace("ENTER: updateStatus; orderId={{}}, newStatus={}", orderId, newStatus);
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(orderId);
        } catch (DaoException e) {
            LOG.error("could not find foodOrder for orderId={{}}; Exception:", orderId, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        FoodOrderStatus oldStatus = foodOrder.getStatus();
        if (oldStatus == newStatus) {
            LOG.debug("status unchanged; orderId={{}}, status={}", orderId, newStatus);
            FoodOrderResponse response = responseMapper.getFoodOrderResponse(foodOrder);
            LOG.trace("EXIT: updateStatus; orderId={{}} (unchanged)", orderId);
            return response;
        }

        foodOrder.setStatus(newStatus);
        foodOrderDao.persist(foodOrder);
        LOG.info("updated order status; orderId={{}}, oldStatus={}, newStatus={}", orderId, oldStatus, newStatus);

        FoodOrderResponse response = responseMapper.getFoodOrderResponse(foodOrder);
        LOG.trace("EXIT: updateStatus; orderId={{}} response=[{}]", orderId, response);
        return response;
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ServiceException {
        LOG.trace("ENTER: delete; id={{}}", id);
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(id);
        } catch (DaoException e) {
            LOG.error("could not find foodOrder for id={{}}; Exception:", id, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        LOG.info("deleted foodOrder; id={{}}", id);

        LOG.trace("EXIT: delete; id={{}}", id);
        foodOrderDao.delete(foodOrder);
    }

    @Transactional
    @Override
    public void shareOrder(String loginNr, ShareOrderRequest request) throws ServiceException {
        LOG.trace("ENTER: shareOrder; loginNr={{}}, request=[{}]", loginNr, request);
        UUID orderId = request.orderId();
        String sharedLoginNr = request.loginNr();
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(orderId);
        } catch (DaoException e) {
            LOG.error("could not find foodOrder for orderId={{}}; loginNr={{}} Exception:", orderId, loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Account sharedAccount;
        try {
            sharedAccount = accountDao.getByLoginNr(sharedLoginNr);
        } catch (DaoException e) {
            LOG.error("could not find shared account for sharedLoginNr={{}}; orderId={{}} Exception:", sharedLoginNr, orderId, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodOrder.setSharedAccount(sharedAccount);
        foodOrderDao.persist(foodOrder);
        LOG.info("shared order; orderId={{}}, fromLoginNr={{}}, toLoginNr={{}}", orderId, loginNr, sharedLoginNr);
        LOG.trace("EXIT: shareOrder; orderId={{}}", orderId);
    }

    @Override
    public FoodOrderResponseFull getById(UUID id) throws ServiceException {
        LOG.trace("ENTER: getById; id={{}}", id);
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(id);
        } catch (DaoException e) {
            LOG.error("could not find foodOrder for id={{}}; Exception:", id, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        FoodOrderResponseFull response = responseMapper.getFoodOrderResponseFullWithNotification(foodOrder);
        LOG.trace("EXIT: getById; id={{}} response=[{}]", id, response);
        return response;
    }

    @Override
    public List<FoodOrderResponse> listAll() {
        LOG.trace("ENTER: listAll");

        List<FoodOrderResponse> responses = foodOrderDao.listAll().stream()//
                .map(responseMapper::getFoodOrderResponse)//
                .toList()//
        ;

        LOG.trace("EXIT: listAll found {} orders", responses.size());
        return responses;
    }

    @Override
    public List<FoodOrderResponse> listByLoginNrAndAccountType(String loginNr, AccountType accountType) throws ServiceException {
        LOG.trace("ENTER: listByLoginNrAndAccountType; loginNr={{}}, accountType={}", loginNr, accountType);

        List<FoodOrder> foodOrders;
        if (accountType == AccountType.ADMIN) {
            LOG.info("Listing all Food Orders (ADMIN); loginNr={{}}", loginNr);
            foodOrders = foodOrderDao.listAll();
        } else if (accountType == AccountType.FOOD_COURT_WORKER) {
            LOG.info("Listing Food Orders by FoodCourt (FOOD_COURT_WORKER); loginNr={{}}", loginNr);
            FoodCourt foodCourt;
            try {
                foodCourt = foodCourtDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                LOG.error("could not find foodCourt for worker loginNr={{}}; Exception:", loginNr, e);
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            UUID foodCourtId = foodCourt.getId();
            foodOrders = foodOrderDao.listByFoodCourtId(foodCourtId);
        } else if (accountType == AccountType.GUEST) {
            LOG.info("Listing Food Orders by loginNr (GUEST); loginNr={{}}", loginNr);
            foodOrders = foodOrderDao.listByLoginNr(loginNr);
        } else {
            LOG.error("unknown accountType={}; loginNr={{}}", accountType, loginNr);
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        List<FoodOrderResponse> responses = foodOrders.stream()//
                .map(responseMapper::getFoodOrderResponse)//
                .collect(Collectors.toList())//
        ;

        LOG.trace("EXIT: listByLoginNrAndAccountType; loginNr={{}} found {} orders", loginNr, responses.size());
        return responses;
    }

    @Override
    public List<FoodOrderResponse> listByLoginNrAndAccountTypeAndStatus(String loginNr, AccountType accountType, FoodOrderStatus status) throws ServiceException {
        LOG.trace("ENTER: listByLoginNrAndAccountTypeAndStatus; loginNr={{}}, accountType={}, status={}", loginNr, accountType, status);
        List<FoodOrder> foodOrders;
        if (accountType == AccountType.ADMIN) {
            LOG.info("Listing Food Orders by status (ADMIN); status={}", status);
            foodOrders = foodOrderDao.listAllByStatus(status);

        } else if (accountType == AccountType.FOOD_COURT_WORKER) {
            LOG.info("Listing Food Orders by FoodCourt + status (FOOD_COURT_WORKER); loginNr={{}}, status={}", loginNr, status);
            FoodCourt foodCourt;
            try {
                foodCourt = foodCourtDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                LOG.error("could not find foodCourt for worker loginNr={{}}; Exception:", loginNr, e);
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            UUID foodCourtId = foodCourt.getId();
            foodOrders = foodOrderDao.listByFoodCourtIdAndStatus(foodCourtId, status);

            LOG.debug("found {} orders for foodCourtId={{}} with status={}", foodOrders.size(), foodCourtId, status);
        } else if (accountType == AccountType.GUEST) {
            LOG.info("Listing Food Orders by loginNr + status (GUEST); loginNr={{}}, status={}", loginNr, status);
            foodOrders = foodOrderDao.listByLoginNrAndStatus(loginNr, status);
            LOG.debug("found {} orders for loginNr={{}} with status={}", foodOrders.size(), loginNr, status);

        } else {
            LOG.error("unknown accountType={}; loginNr={{}}", accountType, loginNr);
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        List<FoodOrderResponse> responses = foodOrders.stream()//
                .map(responseMapper::getFoodOrderResponse)//
                .toList()//
                ;

        LOG.trace("EXIT: listByLoginNrAndAccountTypeAndStatus; loginNr={{}} found {} orders", loginNr, responses.size());
        return responses;
    }

    @Override
    public List<FoodOrderResponseHistory> listByLoginNrAndAccountTypeWithHistory(String loginNr, AccountType accountType) throws ServiceException {
        LOG.trace("ENTER: listByLoginNrAndAccountTypeWithHistory; loginNr={{}}, accountType={}", loginNr, accountType);

        List<FoodOrder> foodOrders;
        if (accountType == AccountType.ADMIN) {
            LOG.info("Listing all Food Orders (ADMIN); loginNr={{}}", loginNr);
            foodOrders = foodOrderDao.listAll();
        } else if (accountType == AccountType.FOOD_COURT_WORKER) {
            LOG.info("Listing Food Orders by FoodCourt (FOOD_COURT_WORKER); loginNr={{}}", loginNr);
            FoodCourt foodCourt;
            try {
                foodCourt = foodCourtDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                LOG.error("could not find foodCourt for worker loginNr={{}}; Exception:", loginNr, e);
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            UUID foodCourtId = foodCourt.getId();
            foodOrders = foodOrderDao.listByFoodCourtId(foodCourtId);
        } else if (accountType == AccountType.GUEST) {
            LOG.info("Listing Food Orders by loginNr (GUEST); loginNr={{}}", loginNr);
            foodOrders = foodOrderDao.listByLoginNr(loginNr);
        } else {
            LOG.error("unknown accountType={}; loginNr={{}}", accountType, loginNr);
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        List<FoodOrderResponseHistory> responses = foodOrders.stream()//
                .map(responseMapper::FoodOrderResponseHistory)//
                .collect(Collectors.toList())//
                ;

        LOG.trace("EXIT: listByLoginNrAndAccountType; loginNr={{}} found {} orders", loginNr, responses.size());
        return responses;
    }

    @Override
    public List<FoodOrderResponseHistory> listByLoginNrAndAccountTypeAndStatusWithHistory(String loginNr, AccountType accountType, FoodOrderStatus status) throws ServiceException {
        LOG.trace("ENTER: listByLoginNrAndAccountTypeAndStatusWithHistory; loginNr={{}}, accountType={}, status={}", loginNr, accountType, status);
        List<FoodOrder> foodOrders;
        if (accountType == AccountType.ADMIN) {
            LOG.info("Listing Food Orders by status (ADMIN); status={}", status);
            foodOrders = foodOrderDao.listAllByStatus(status);

        } else if (accountType == AccountType.FOOD_COURT_WORKER) {
            LOG.info("Listing Food Orders by FoodCourt + status (FOOD_COURT_WORKER); loginNr={{}}, status={}", loginNr, status);
            FoodCourt foodCourt;
            try {
                foodCourt = foodCourtDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                LOG.error("could not find foodCourt for worker loginNr={{}}; Exception:", loginNr, e);
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            UUID foodCourtId = foodCourt.getId();
            foodOrders = foodOrderDao.listByFoodCourtIdAndStatus(foodCourtId, status);

            LOG.debug("found {} orders for foodCourtId={{}} with status={}", foodOrders.size(), foodCourtId, status);
        } else if (accountType == AccountType.GUEST) {
            LOG.info("Listing Food Orders by loginNr + status (GUEST); loginNr={{}}, status={}", loginNr, status);
            foodOrders = foodOrderDao.listByLoginNrAndStatus(loginNr, status);
            LOG.debug("found {} orders for loginNr={{}} with status={}", foodOrders.size(), loginNr, status);

        } else {
            LOG.error("unknown accountType={}; loginNr={{}}", accountType, loginNr);
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }

        List<FoodOrderResponseHistory> responses = foodOrders.stream()//
                .map(responseMapper::FoodOrderResponseHistory)//
                .toList()//
                ;

        LOG.trace("EXIT: listByLoginNrAndAccountTypeAndStatusWithHistory; loginNr={{}} found {} orders", loginNr, responses.size());
        return responses;
    }
}
