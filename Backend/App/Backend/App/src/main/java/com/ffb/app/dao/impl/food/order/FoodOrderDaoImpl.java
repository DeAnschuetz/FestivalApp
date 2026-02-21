package com.ffb.app.dao.impl.food.order;

import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.app.repository.api.food.order.FoodOrderHistoryRepository;
import com.ffb.app.repository.api.food.order.FoodOrderItemRepository;
import com.ffb.app.repository.api.food.order.FoodOrderRepository;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderHistory;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodOrderDaoImpl implements FoodOrderDao {

    private final FoodOrderRepository foodOrderRepo;
    private final FoodOrderItemRepository foodOrderItemRepo;
    private final FoodOrderHistoryRepository foodOrderHistoryRepo;

    @Inject
    public FoodOrderDaoImpl(
            FoodOrderRepository foodOrderRepo,
            FoodOrderItemRepository foodOrderItemRepo,
            FoodOrderHistoryRepository foodOrderHistoryRepo
    ) {
        this.foodOrderRepo = foodOrderRepo;
        this.foodOrderItemRepo = foodOrderItemRepo;
        this.foodOrderHistoryRepo = foodOrderHistoryRepo;
    }

    @Override
    public List<FoodOrder> listAllWithItems() {
        return foodOrderRepo.listAllWithItems();
    }

    @Override
    public FoodOrder findByIdWithItems(UUID id) throws DaoException {
        return foodOrderRepo.findByIdWithItems(id)
                .orElseThrow(() -> new DaoException("Food order with id " + id + " not found."))
        ;
    }

    @Override
    public FoodOrder findByIdWithItemsAndHistory(UUID id) throws DaoException {
        return foodOrderRepo.findByIdWithItemsAndHistory(id)
                .orElseThrow(() -> new DaoException("Food order with id " + id + " not found."))
        ;
    }

    @Override
    public List<FoodOrder> listByLoginNr(String loginNr) {
        return foodOrderRepo.listByLoginNr(loginNr);
    }

    @Override
    public List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status) {
        return foodOrderRepo.listByLoginNrAndStatus(loginNr, status);
    }

    @Override
    public List<FoodOrderHistory> listHistoryByFoodOrderId(UUID foodOrderId) {
        return foodOrderHistoryRepo.listByFoodOrderId(foodOrderId);
    }

    @Override
    public List<FoodOrderItem> listItemsByFoodOrderId(UUID foodOrderId) {
        return foodOrderItemRepo.listByFoodOrderId(foodOrderId);
    }

    @Override
    public List<FoodOrder> listAll() {
        return foodOrderRepo.listAll();
    }

    @Override
    public FoodOrder findById(UUID id) {
        return foodOrderRepo.findById(id);
    }

    @Override
    public void persistHistory(FoodOrderHistory history) {
        foodOrderHistoryRepo.persist(history);
    }

    @Override
    public void persist(FoodOrder foodOrder) {
        foodOrderRepo.persist(foodOrder);
    }

    @Override
    public void delete(FoodOrder foodOrder) {
        foodOrderRepo.delete(foodOrder);
    }

    @Override
    public void persistItem(FoodOrderItem foodOrderItem) {
        foodOrderItemRepo.persist(foodOrderItem);
    }
}
