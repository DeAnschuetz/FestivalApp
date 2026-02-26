package com.ffb.app.service.api.impl.food.court;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.foodorder.FoodOrder;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtSimulationService {

    private static final Logger LOG = Logger.getLogger(FoodCourtSimulationService.class);

    @ConfigProperty(name = "foodcourt.preparation.speed")
    int FOOD_PREPARATION_SPEED;

    private final FoodOrderDao foodOrderDao;
    private final FoodCourtDao foodCourtDao;

    public FoodCourtSimulationService(FoodOrderDao foodOrderDao, FoodCourtDao foodCourtDao) {
        this.foodOrderDao = foodOrderDao;
        this.foodCourtDao = foodCourtDao;
    }

    @Scheduled(
            every = "{foodcourt.simmulation.speed}",
            concurrentExecution = Scheduled.ConcurrentExecution.SKIP
    )
    @Transactional
    void simulateOrderProcessing() {
        LOG.info("Simulating Order processing");
        List<FoodCourt> foodCourts = foodCourtDao.listAll();
        foodCourts.stream()//
                .forEach(foodCourt -> {
                    LOG.info("Simulating Order processing " + foodCourt.getDisplayName());
                    UUID foodCourtId = foodCourt.getId();
                    List<FoodOrder> foodOrders = foodOrderDao.listByFoodCourtId(foodCourtId);
                    int ordersInProgress = 0;
                    int i = 0;
                    if (foodOrders.isEmpty()) {
                        LOG.info("No orders");
                        return;
                    }
                    while (i < foodOrders.size() && ordersInProgress < 5) {
                        FoodOrder foodOrder = foodOrders.get(i);
                        ordersInProgress += processOrder(foodOrder);
                        i++;
                    }
                    LOG.info("Orders in Progress: " + ordersInProgress);
                })//
        ;
        LOG.info("Finished Order processing");
    }

    private int processOrder(FoodOrder order) {
        if (order.getStatus() == FoodOrderStatus.IN_PROGRESS) {
            LOG.info("Order " + order.getId().toString() + " is READY_FOR_PICKUP");
            order.setStatus(FoodOrderStatus.READY_FOR_PICKUP);
            return -1;
        } else if (order.getStatus() == FoodOrderStatus.ORDERED) {
            LOG.info("Order " + order.getId().toString() + " is IN_PROGRESS");
            order.setStatus(FoodOrderStatus.IN_PROGRESS);
            return 1;
        }
        return 0;
    }
}