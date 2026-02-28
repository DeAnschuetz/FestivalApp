package com.ffb.app.dao.impl.food.order;

import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.app.repository.api.food.order.FoodOrderHistoryRepository;
import com.ffb.app.repository.api.food.order.FoodOrderItemRepository;
import com.ffb.app.repository.api.food.order.FoodOrderRepository;
import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderHistory;
import com.ffb.model.db.object.foodorder.FoodOrderItem;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodOrderDaoImpl implements FoodOrderDao {

    // TODO Logging

    private final FoodOrderRepository foodOrderRepo;

    @Inject
    public FoodOrderDaoImpl(FoodOrderRepository foodOrderRepo) {
        this.foodOrderRepo = foodOrderRepo;
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
    public List<FoodOrder> listAllByStatus(FoodOrderStatus status) {
        return foodOrderRepo.listAllByStatus(status);
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
    public List<FoodOrder> listByFoodCourtId(UUID foodCourtId) {
        return foodOrderRepo.listByFoodCourtId(foodCourtId);
    }

    @Override
    public List<FoodOrder> listByFoodCourtIdAndStatus(UUID foodCourtId, FoodOrderStatus status) {
        return foodOrderRepo.listByFoodCourtIdAndStatus(foodCourtId, status);
    }

    @Override
    public void persist(FoodOrder foodOrder) {
        foodOrderRepo.persist(foodOrder);
    }

    @Override
    public void delete(FoodOrder foodOrder) {
        foodOrderRepo.delete(foodOrder);
    }
}
