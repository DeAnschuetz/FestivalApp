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
    public FoodOrder getById(UUID id) throws DaoException {
        return foodOrderRepo.getById(id)//
                .orElseThrow(() -> new DaoException("Food order with id " + id + " not found."))//
        ;

    }

    @Override
    public FoodOrder getByIdWithItems(UUID id) throws DaoException {
        return foodOrderRepo.getByIdWithItems(id)
                .orElseThrow(() -> new DaoException("Food order with id " + id + " not found."))
        ;
    }

    @Override
    public FoodOrder getByIdWithItemsAndHistory(UUID id) throws DaoException {
        return foodOrderRepo.getByIdWithItemsAndHistory(id)
                .orElseThrow(() -> new DaoException("Food order with id " + id + " not found."))
        ;
    }

    @Override
    public List<FoodOrder> listAll() {
        return foodOrderRepo.listAll();
    }

    @Override
    public List<FoodOrder> listAllWithItems() {
        return foodOrderRepo.listAllWithItems();
    }

    @Override
    public List<FoodOrder> listAllByStatus(FoodOrderStatus status) {
        return foodOrderRepo.listAllByStatus(status);
    }

    @Override
    public List<FoodOrder> listAllWithItemsByStatus(FoodOrderStatus status) {
        return foodOrderRepo.listAllByStatusWithItems(status);
    }

    @Override
    public List<FoodOrder> listByLoginNr(String loginNr) {
        return foodOrderRepo.listByLoginNr(loginNr);
    }

    @Override
    public List<FoodOrder> listByLoginNrWithItems(String loginNr) {
        return foodOrderRepo.listByLoginNrWithItems(loginNr);
    }

    @Override
    public List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status) {
        return foodOrderRepo.listByLoginNrAndStatus(loginNr, status);
    }

    @Override
    public List<FoodOrder> listByLoginNrAndStatusWithItems(String loginNr, FoodOrderStatus status) {
        return foodOrderRepo.listByLoginNrAndStatusWithItems(loginNr, status);
    }

    @Override
    public List<FoodOrder> listByFoodCourtId(UUID foodCourtId) {
        return foodOrderRepo.listByFoodCourtId(foodCourtId);
    }

    @Override
    public List<FoodOrder> listByFoodCourtIdWithItems(UUID foodCourtId) {
        return foodOrderRepo.listByFoodCourtIdWithItems(foodCourtId);
    }

    @Override
    public List<FoodOrder> listByFoodCourtIdAndStatus(UUID foodCourtId, FoodOrderStatus status) {
        return foodOrderRepo.listByFoodCourtIdAndStatus(foodCourtId, status);
    }

    @Override
    public List<FoodOrder> listByFoodCourtIdAndStatusWithItems(UUID foodCourtId, FoodOrderStatus status) {
        return foodOrderRepo.listByFoodCourtIdAndStatusWithItems(foodCourtId, status);
    }

    @Override
    public List<FoodOrderItem> listItemsByFoodOrderId(UUID foodOrderId) {
        return foodOrderItemRepo.listByFoodOrderId(foodOrderId);
    }

    @Override
    public List<FoodOrderHistory> listHistoryByFoodOrderId(UUID foodOrderId) {
        return foodOrderHistoryRepo.listByFoodOrderId(foodOrderId);
    }

    @Override
    public void persist(FoodOrder foodOrder) {
        foodOrderRepo.persist(foodOrder);
    }

    @Override
    public void persistHistory(FoodOrderHistory history) {
        foodOrderHistoryRepo.persist(history);
    }

    @Override
    public void persistItem(FoodOrderItem foodOrderItem) {
        foodOrderItemRepo.persist(foodOrderItem);
    }

    @Override
    public void delete(FoodOrder foodOrder) {
        foodOrderRepo.delete(foodOrder);
    }
}
