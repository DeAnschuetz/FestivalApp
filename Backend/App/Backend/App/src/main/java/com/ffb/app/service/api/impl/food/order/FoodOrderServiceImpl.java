package com.ffb.app.service.api.impl.food.order;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.app.service.api.api.food.order.FoodOrderService;
import com.ffb.app.service.api.impl.food.court.FoodCourtSimulationService;
import com.ffb.model.api.response.order.FoodOrderItemResponse;
import com.ffb.model.api.response.order.FoodOrderResponse;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderHistory;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FoodOrderServiceImpl implements FoodOrderService {


    private static final Logger LOG = Logger.getLogger(FoodOrderService.class);

    @ConfigProperty(name = "order.extra.price")
    double EXTRA_PRICE;

    private final FoodOrderDao foodOrderDao;
    private final AccountDao accountDao;
    private final CartDao cartDao;
    private final FoodCourtDao foodCourtDao;
    private final CreditDao creditDao;

    @Inject
    public FoodOrderServiceImpl(
            FoodOrderDao foodOrderDao,
            AccountDao accountDao,
            CartDao cartDao,
            FoodCourtDao foodCourtDao,
            CreditDao creditDao
    ) {
        this.foodOrderDao = foodOrderDao;
        this.accountDao = accountDao;
        this.cartDao = cartDao;
        this.foodCourtDao = foodCourtDao;
        this.creditDao = creditDao;
    }

    @Transactional
    @Override
    public List<FoodOrderResponse> create(String loginNr) throws ServiceException {
        try {
            Account account = accountDao.findByLoginNr(loginNr);
            Cart cart = cartDao.findByLoginNrWithItems(loginNr);
            List<CartItem> cartItems = cart.getCartItems();
            return cartItems.stream()//
                    .collect(
                            Collectors.groupingBy(
                                    item -> item.getProduct().getFoodCourt().getId(),
                                    Collectors.mapping(item -> {
                                                Product currentProduct = item.getProduct();
                                                String extra = item.getExtra();

                                                double price = extra.isBlank()
                                                        ? currentProduct.getPrice()
                                                        : currentProduct.getPrice() + EXTRA_PRICE;

                                                FoodOrderItem foodOrderItem = new FoodOrderItem(
                                                        UUID.randomUUID(),
                                                        price,
                                                        item.getItemCount(),
                                                        extra,
                                                        item.getProduct()
                                                );
                                                return foodOrderItem;
                                            },
                                            Collectors.toList()
                                    )//
                            )//
                    )//
                    .entrySet().stream()//
                    .map(entry -> {
                                List<FoodOrderItem> foodOrderItems = entry.getValue();
                                double total = foodOrderItems.stream()//
                                        .mapToDouble(item -> item.getPrice() * item.getItemCount())//
                                        .sum()//
                                        ;
                                FoodCourt foodCourt;
                                try {
                                    foodCourt = foodCourtDao.getById(entry.getKey());
                                } catch (DaoException e) {
                                    throw new RuntimeException(e);
                                }
                                FoodOrder foodOrder = new FoodOrder(
                                        UUID.randomUUID(),
                                        FoodOrderStatus.ORDERED,
                                        cart.isHasPrio(),
                                        total,
                                        foodCourt.getWaitingTimeObject().getWaitingTime(),
                                        foodOrderItems,
                                        foodCourt,
                                        account
                                );
                                FoodOrderHistory history = new FoodOrderHistory(
                                        UUID.randomUUID(),
                                        foodOrder,
                                        LocalDateTime.now(),
                                        null,
                                        FoodOrderStatus.ORDERED
                                );
                                foodOrderItems.forEach(foodOrderItem -> {
                                    foodOrderItem.setFoodOrder(foodOrder);
                                });

                                Credit currentCredit;
                                try {
                                    currentCredit = creditDao.getByLoginNr(loginNr);
                                } catch (DaoException e) {
                                    throw new RuntimeException(e);
                                }
                                double currentCreditAmount = currentCredit.getAmount();
                                if (currentCreditAmount < total) {
                                    throw new RuntimeException("Insufficient credit");
                                }
                                currentCredit.setAmount(currentCreditAmount - total);

                                foodCourtDao.persist(foodCourt);
                                creditDao.persist(currentCredit);
                                foodOrderDao.persist(foodOrder);
                                cartDao.empty(cart);
                                return foodOrder;
                            }//
                    )//
                    .map(this::getFoodOrderResponse)
                    .toList()//
            ;
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

    }

    @Transactional
    @Override
    public FoodOrderResponse updateStatus(UUID orderId, FoodOrderStatus newStatus) throws ServiceException {
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        if (foodOrder == null) {
            throw new ServiceException("FoodOrder not found: " + orderId, Response.Status.NOT_FOUND);
        }

        FoodOrderStatus oldStatus = foodOrder.getStatus();
        if (oldStatus == newStatus) {
            return getFoodOrderResponse(foodOrder);
        }

        foodOrder.setStatus(newStatus);
        FoodOrderHistory history = new FoodOrderHistory(
                UUID.randomUUID(),
                foodOrder,
                LocalDateTime.now(),
                oldStatus,
                newStatus
        );

        foodOrderDao.persist(foodOrder);
        return getFoodOrderResponse(foodOrder);
    }

    @Transactional
    @Override
    public void delete(UUID id) throws ServiceException {
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(id);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        if (foodOrder == null) {
            throw new ServiceException("FoodOrder not found: " + id, Response.Status.NOT_FOUND);
        }

        foodOrderDao.delete(foodOrder);
    }

    @Transactional
    @Override
    public void shareOrder(String loginNr, UUID orderId, String sharedLoginNr) throws ServiceException {
        FoodOrder foodOrder;
        try {
            foodOrder = foodOrderDao.getById(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Account sharedAccount;
        try {
            sharedAccount = accountDao.findByLoginNr(sharedLoginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodOrder.setSharedAccount(sharedAccount);
        foodOrderDao.persist(foodOrder);
    }

    @Override
    public FoodOrderResponse getById(UUID id, boolean withItems, boolean withHistory) throws ServiceException {
        FoodOrder foodOrder;
        if (withItems && withHistory) {
            try {
                foodOrder = foodOrderDao.getByIdWithItemsAndHistory(id);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else if (withItems) {
            try {
                foodOrder = foodOrderDao.getByIdWithItems(id);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else {
            try {
                foodOrder = foodOrderDao.getById(id);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        }

        if (foodOrder == null) {
            throw new ServiceException("FoodOrder not found: " + id, Response.Status.NOT_FOUND);
        }
        return getFoodOrderResponse(foodOrder);
    }

    @Override
    public List<FoodOrderResponse> listAll(boolean withItems) {
        List<FoodOrder> foodOrders = withItems ? foodOrderDao.listAllWithItems() : foodOrderDao.listAll();
        return foodOrders.stream()//
                .map(this::getFoodOrderResponse)//
                .toList()//
        ;
    }

    @Override
    public List<FoodOrderResponse> listByLoginNrAndAccountType(String loginNr, AccountType accountType, boolean withItems) throws ServiceException {
        List<FoodOrder> foodOrders;
        if (accountType == AccountType.ADMIN) {
            LOG.info("Listing all Food Orders (ADMIN)");
            if (withItems) {
                foodOrders = foodOrderDao.listAllWithItems();
            } else {
                foodOrders = foodOrderDao.listAll();
            }
        } else if (accountType == AccountType.FOOD_COURT_WORKER) {
            LOG.info("Listing all Food Orders (FOOD_COURT_WORKER)");
            FoodCourt foodCourt;
            try {
                foodCourt = foodCourtDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            UUID foodCourtId = foodCourt.getId();
            if (withItems) {
                foodOrders = foodOrderDao.listByFoodCourtIdWithItems(foodCourtId);
            } else {
                foodOrders = foodOrderDao.listByFoodCourtId(foodCourtId);
            }
        } else if (accountType == AccountType.GUEST) {
            LOG.info("Listing all Food Orders (GUEST)");
            if (withItems) {
                foodOrders = foodOrderDao.listByLoginNrWithItems(loginNr);
            } else {
                foodOrders = foodOrderDao.listByLoginNr(loginNr);
            }
        } else {
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        return foodOrders.stream().map(this::getFoodOrderResponse).collect(Collectors.toList());
    }

    @Override
    public List<FoodOrderResponse> listByLoginNrAndAccountTypeAndStatus(String loginNr, AccountType accountType, FoodOrderStatus status, boolean withItems) throws ServiceException {
        List<FoodOrder> foodOrders;
        if (accountType == AccountType.ADMIN) {
            if (withItems) {
                foodOrders = foodOrderDao.listAllByStatus(status);
            } else {
                foodOrders = foodOrderDao.listAllWithItemsByStatus(status);
            }
        } else if (accountType == AccountType.FOOD_COURT_WORKER) {
            FoodCourt foodCourt;
            try {
                foodCourt = foodCourtDao.getByLoginNr(loginNr);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
            UUID foodCourtId = foodCourt.getId();
            if (withItems) {
                foodOrders = foodOrderDao.listByFoodCourtIdAndStatusWithItems(foodCourtId, status);
            } else {
                foodOrders = foodOrderDao.listByFoodCourtIdAndStatus(foodCourtId, status);
            }
        } else if (accountType == AccountType.GUEST) {
            if (withItems) {
                foodOrders = foodOrderDao.listByLoginNrAndStatusWithItems(loginNr, status);
            } else {
                foodOrders = foodOrderDao.listByLoginNrAndStatus(loginNr, status);
            }
        } else {
            throw new ServiceException("Unknown Account type: " + accountType.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        }
        return foodOrders.stream()//
                .map(this::getFoodOrderResponse)//
                .toList()//
        ;
    }

    /*
        Private Helper Functions
    */

    private FoodOrderResponse getFoodOrderResponse(FoodOrder foodOrder) {
        List<FoodOrderItemResponse> foodOrderItems = foodOrder.getFoodOrderItems().stream().map(this::getFoodOrderItemResponse).toList();
        return new FoodOrderResponse(
                foodOrder.getId(),
                foodOrder.getStatus(),
                foodOrder.getFoodCourt().getDisplayName(),
                foodOrder.getWaitingTime(),
                foodOrderItems
        );
    }

    private FoodOrderItemResponse getFoodOrderItemResponse(FoodOrderItem foodOrderItem) {
        Product product = foodOrderItem.getProduct();
        List<FoodOrderItemResponse> subItems = product.getSubProducts().stream().map(subProduct -> getFoodOrderItemResponse(subProduct, foodOrderItem.getItemCount())).toList();
        return new FoodOrderItemResponse(
                product.getId(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                foodOrderItem.getItemCount(),
                foodOrderItem.getExtra(),
                subItems
        );
    }

    private FoodOrderItemResponse getFoodOrderItemResponse(Product product, int count) {
        return new FoodOrderItemResponse(
                product.getId(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                count,
                null,
                null
        );
    }
}
