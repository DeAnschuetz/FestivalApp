package com.ffb.app.service.api.impl.food.order;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.app.service.api.api.food.order.FoodOrderService;
import com.ffb.model.db.objects.account.Account;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FoodOrderServiceImpl implements FoodOrderService {

    private final double EXTRA_PRICE = 2;

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

    @Override
    public List<FoodOrder> listAll(boolean withItems) {
        return withItems ? foodOrderDao.listAllWithItems() : foodOrderDao.listAll();
    }

    @Override
    public List<FoodOrder> listByLoginNr(String loginNr) {
        return foodOrderDao.listByLoginNr(loginNr);
    }


    @Override
    public List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status) {
        return foodOrderDao.listByLoginNrAndStatus(loginNr, status);
    }


    @Override
    public FoodOrder getById(UUID id, boolean withItems, boolean withHistory) throws ServiceException {
        FoodOrder foodOrder;
        if (withItems && withHistory) {
            try {
                foodOrder = foodOrderDao.findByIdWithItemsAndHistory(id);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else if (withItems) {
            try {
                foodOrder = foodOrderDao.findByIdWithItems(id);
            } catch (DaoException e) {
                throw new ServiceException(e, Response.Status.NOT_FOUND);
            }
        } else {
            foodOrder = foodOrderDao.findById(id);
        }

        if (foodOrder == null) {
            throw new ServiceException("FoodOrder not found: " + id, Response.Status.NOT_FOUND);
        }
        return foodOrder;
    }

    @Override
    @Transactional
    public List<FoodOrder> create(String loginNr) throws ServiceException {
        try {
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
                                                        extra
                                                );
                                                foodOrderDao.persistItem(foodOrderItem);
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
                                FoodCourt foodCourt = null;
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
                                        foodCourt.getWaitingTime().getWaitingTime(),
                                        foodOrderItems
                                );
                                FoodOrderHistory history = new FoodOrderHistory(
                                        UUID.randomUUID(),
                                        foodOrder,
                                        LocalDateTime.now(),
                                        null,
                                        FoodOrderStatus.ORDERED
                                );
                                Credit currentCredit = null;
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

                                creditDao.persist(currentCredit);
                                foodOrderDao.persistHistory(history);
                                foodOrderDao.persist(foodOrder);
                                return foodOrder;
                            }//
                    )//
                    .toList()//
                    ;
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

    }

    @Override
    @Transactional
    public FoodOrder updateStatus(UUID orderId, FoodOrderStatus newStatus) throws ServiceException {
        FoodOrder foodOrder = foodOrderDao.findById(orderId);
        if (foodOrder == null) {
            throw new ServiceException("FoodOrder not found: " + orderId, Response.Status.NOT_FOUND);
        }

        FoodOrderStatus oldStatus = foodOrder.getStatus();
        if (oldStatus == newStatus) {
            return foodOrder;
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
        return foodOrder;
    }

    @Override
    @Transactional
    public void delete(UUID id) throws ServiceException {
        FoodOrder foodOrder = foodOrderDao.findById(id);
        if (foodOrder == null) {
            throw new ServiceException("FoodOrder not found: " + id, Response.Status.NOT_FOUND);
        }

        foodOrderDao.delete(foodOrder);
    }

    @Override
    public void shareOrder(String loginNr, UUID orderId, String sharedLoginNr) throws ServiceException {
        FoodOrder foodOrder = foodOrderDao.findById(orderId);
        Account sharedAccount;
        try {
            sharedAccount = accountDao.findByLoginNr(sharedLoginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        foodOrder.setSharedAccount(sharedAccount);
        foodOrderDao.persist(foodOrder);
    }
}
