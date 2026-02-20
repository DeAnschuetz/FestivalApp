package com.ffb.app.service.impl.food.order;

import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.app.repository.api.cart.CartRepository;
import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.app.repository.api.food.court.FoodCourtRepository;
import com.ffb.app.repository.api.food.order.FoodOrderHistoryRepository;
import com.ffb.app.repository.api.food.order.FoodOrderItemRepository;
import com.ffb.app.repository.api.food.order.FoodOrderRepository;
import com.ffb.app.service.api.food.order.FoodOrderService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderHistory;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import com.ffb.model.db.objects.product.MainProduct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FoodOrderServiceImpl implements FoodOrderService {

    private final double EXTRA_PRICE = 2;

    private final FoodOrderRepository foodOrderRepo;
    private final FoodOrderItemRepository foodOrderItemRepo;
    private final FoodOrderHistoryRepository foodOrderHistoryRepo;
    private final AccountRepository accountRepo;
    private final CartRepository cartRepo;
    private final FoodCourtRepository foodCourtRepo;
    private final CreditRepository creditRepo;

    @Inject
    public FoodOrderServiceImpl(
            FoodOrderRepository foodOrderRepo,
            FoodOrderItemRepository foodOrderItemRepo,
            FoodOrderHistoryRepository foodOrderHistoryRepo,
            AccountRepository accountRepo,
            CartRepository cartRepo,
            FoodCourtRepository foodCourtRepo,
            CreditRepository creditRepo
    ) {
        this.foodOrderRepo = foodOrderRepo;
        this.foodOrderItemRepo = foodOrderItemRepo;
        this.foodOrderHistoryRepo = foodOrderHistoryRepo;
        this.accountRepo = accountRepo;
        this.cartRepo = cartRepo;
        this.foodCourtRepo = foodCourtRepo;
        this.creditRepo = creditRepo;
    }
    public List<FoodOrder> listAll(boolean withItems) {
        return withItems ? foodOrderRepo.listAllWithItems() : foodOrderRepo.listAll();
    }

    @Override
    public List<FoodOrder> listByLoginNr(String loginNr) {
        return foodOrderRepo.listByLoginNr(loginNr);
    }


    @Override
    public List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status) {
        return foodOrderRepo.listByLoginNrAndStatus(loginNr, status);
    }


    public FoodOrder getById(UUID id, boolean withItems, boolean withHistory) {
        FoodOrder foodOrder = null;
        if (withItems && withHistory) {
            foodOrder = foodOrderRepo.findByIdWithItemsAndHistory(id)//
                    .orElseThrow(() -> new NotFoundException("No food order with id " + id))//
            ;
        } else if (withItems) {
            foodOrder = foodOrderRepo.findByIdWithItems(id)//
                    .orElseThrow(() -> new NotFoundException("No food order with id " + id))//
            ;
        } else {
            foodOrder = foodOrderRepo.findById(id);
        }

        if (foodOrder == null) throw new NotFoundException("FoodOrder not found: " + id);
        return foodOrder;
    }

    @Transactional
    public List<FoodOrder> create(String loginNr) {
        Cart cart = cartRepo.findByLoginNr(loginNr)//
                .orElseThrow(() -> new NotFoundException("Cart not found for loginNr: " + loginNr))//
        ;
        List<CartItem> cartItems = cart.getCartItems();

        return cartItems.stream()//
                .collect(
                    Collectors.groupingBy(
                        item -> item.getProduct().getFoodCourt().getId(),
                        Collectors.mapping(item -> {
                                MainProduct currentProduct = item.getProduct();
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
                                foodOrderItemRepo.persist(foodOrderItem);
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
                        FoodCourt foodCourt = foodCourtRepo.findByIdOptional(entry.getKey())
                                .orElseThrow(() -> new NotFoundException("FoodCourt not found: " + entry.getKey()))//
                        ;
                        FoodOrder foodOrder = new FoodOrder(
                                UUID.randomUUID(),
                                FoodOrderStatus.ORDERED,
                                cart.isHasPrio(),
                                total,
                                foodCourt.getWaitingTime(),
                                foodOrderItems
                        );
                        FoodOrderHistory history = new FoodOrderHistory(
                                UUID.randomUUID(),
                                foodOrder,
                                LocalDateTime.now(),
                                null,
                                FoodOrderStatus.ORDERED
                        );
                        Credit currentCredit = creditRepo.findByLoginNr(loginNr)
                                .orElseThrow(() -> new NotFoundException("Credit not found for loginNr: " + loginNr))//
                        ;
                        double currentCreditAmount = currentCredit.getAmount();
                        if (currentCreditAmount < total) {
                            throw new IllegalStateException("Insufficient credit");
                        }
                        currentCredit.setAmount(currentCreditAmount - total);

                        creditRepo.persist(currentCredit);
                        foodOrderHistoryRepo.persist(history);
                        foodOrderRepo.persist(foodOrder);
                        return foodOrder;
                    }//
                )//
                .toList()//
        ;
    }

    @Transactional
    public FoodOrder updateStatus(UUID orderId, FoodOrderStatus newStatus) {
        FoodOrder foodOrder = foodOrderRepo.findById(orderId);
        if (foodOrder == null) {
            throw new NotFoundException("FoodOrder not found: " + orderId);
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

        foodOrderRepo.persist(foodOrder);
        return foodOrder;
    }

    @Transactional
    public void delete(UUID id) {
        FoodOrder foodOrder = foodOrderRepo.findById(id);
        if (foodOrder == null) {
            throw new NotFoundException("FoodOrder not found: " + id);
        }

        foodOrderRepo.delete(foodOrder);
    }

    @Override
    public void shareOrder(String loginNr, UUID orderId, String sharedLoginNr) {
        FoodOrder foodOrder = foodOrderRepo.findById(orderId);
        Account sharedAccount = accountRepo.findByLoginNr(sharedLoginNr)//
                .orElseThrow(() -> new NotFoundException("Account not found: " + sharedLoginNr))//
        ;
        foodOrder.setSharedAccount(sharedAccount);
        foodOrderRepo.persist(foodOrder);
    }
}
