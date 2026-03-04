package com.ffb.app.repository.impl.food.order;

import com.ffb.app.repository.api.food.order.FoodOrderRepository;
import com.ffb.app.service.api.food.order.FoodOrderService;
import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FoodOrderRepositoryImpl implements FoodOrderRepository {

    // TODO Logging
    private static final Logger LOG = LoggerFactory.getLogger(FoodOrderRepository.class);

    @Override
    public List<FoodOrder> listAll() {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo"
                )//
                .list()//
        ;
    }

    @Override
    public List<FoodOrder> listAllByStatus(FoodOrderStatus status) {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo " +
                    "WHERE fo.status = ?1",
                    status
                )//
                .list()//
        ;
    }

    @Override
    public Optional<FoodOrder> getById(UUID id) {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo " +
                    "WHERE fo.id = ?1",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public Optional<FoodOrder> getByIdWithItems(UUID id) {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo " +
                    "LEFT JOIN FETCH fo.foodOrderItems " +
                    "WHERE fo.id = ?1",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public Optional<FoodOrder> getByIdWithItemsAndHistory(UUID id) {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo " +
                    "LEFT JOIN FETCH fo.foodOrderItems " +
                    "LEFT JOIN FETCH fo.foodOrderHistory " +
                    "WHERE fo.id = ?1",
                    id
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public List<FoodOrder> listByLoginNr(String loginNr) {
        LOG.info("loginNR=" + loginNr);
        return find(
                    "SELECT DISTINCT fo " +
                            "FROM FoodOrder fo " +
                            "JOIN fo.account a " +
                            "JOIN a.ticket t " +
                            "LEFT JOIN fo.sharedAccount sa " +
                            "LEFT JOIN sa.ticket sat " +
                            "WHERE t.loginNr = ?1 OR sat.loginNr = ?1",
                    loginNr
                 )//
                .list()//
        ;
    }

    @Override
    public List<FoodOrder> listByLoginNrAndStatus(String loginNr, FoodOrderStatus status) {
        return find(
                "SELECT DISTINCT fo " +
                        "FROM FoodOrder fo " +
                        "JOIN fo.account a " +
                        "JOIN a.ticket t " +
                        "LEFT JOIN fo.sharedAccount sa " +
                        "LEFT JOIN sa.ticket sat " +
                        "WHERE t.loginNr = ?1 OR sat.loginNr = ?1 AND fo.status = ?2",
                    loginNr,
                    status
                )//
                .list()//
        ;
    }

    @Override
    public List<FoodOrder> listByFoodCourtId(UUID foodCourtId) {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo " +
                    "WHERE fo.foodCourt.id = ?1",
                    foodCourtId
                )//
                .list()//
        ;
    }

    @Override
    public List<FoodOrder> listByFoodCourtIdAndStatus(UUID foodCourtId, FoodOrderStatus status) {
        return find(
                    "SELECT DISTINCT fo " +
                    "FROM FoodOrder fo " +
                    "WHERE fo.foodCourt.id = ?1 AND fo.status = ?2",
                    foodCourtId,
                    status
                )//
                .list()//
        ;
    }
}
