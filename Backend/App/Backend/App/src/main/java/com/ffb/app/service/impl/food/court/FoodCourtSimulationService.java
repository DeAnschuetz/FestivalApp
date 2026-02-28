package com.ffb.app.service.impl.food.court;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtSimulationService {

    // TODO Logging
    private static final Logger LOG = LoggerFactory.getLogger(FoodCourtSimulationService.class);

    private final FoodOrderDao foodOrderDao;
    private final FoodCourtDao foodCourtDao;
    private final Scheduler scheduler;


    public FoodCourtSimulationService(FoodOrderDao foodOrderDao, FoodCourtDao foodCourtDao, Scheduler scheduler) {
        this.foodOrderDao = foodOrderDao;
        this.foodCourtDao = foodCourtDao;
        this.scheduler = scheduler;
    }

    @Scheduled(
            identity = "foodCourt-OrderProcessing-Simulation",
            every = "{foodcourt.preparation.time}",
            concurrentExecution = Scheduled.ConcurrentExecution.SKIP
    )
    @Transactional
    void simulateOrderProcessing() {
        LOG.info("Simulating Order processing");
        List<FoodCourt> foodCourts = foodCourtDao.listAll();
        foodCourts.forEach(this::simulateOrderProcessing);
        LOG.info("Finished Order processing");
    }

    private void simulateOrderProcessing(FoodCourt foodCourt) {
        LOG.info("Simulating Order processing for '{}'", foodCourt.getDisplayName());
        UUID foodCourtId = foodCourt.getId();
        List<FoodOrder> foodOrders = foodOrderDao.listByFoodCourtId(foodCourtId);
        int ordersInProgress = 0;
        int i = 0;
        if (foodOrders.isEmpty()) {
            LOG.debug("No orders");
            return;
        }
        while (i < foodOrders.size() && ordersInProgress < 5) {
            FoodOrder foodOrder = foodOrders.get(i);
            ordersInProgress += processOrder(foodOrder);
            i++;
        }
        LOG.debug("Orders in Progress: {}", ordersInProgress);
    }

    private int processOrder(FoodOrder order) {
        LOG.debug("Order {{}} is {}", order.getId(), order.getStatus() );
        if (order.getStatus() == FoodOrderStatus.IN_PROGRESS) {
            order.setStatus(FoodOrderStatus.READY_FOR_PICKUP);
            return -1;
        } else if (order.getStatus() == FoodOrderStatus.ORDERED) {
            order.setStatus(FoodOrderStatus.IN_PROGRESS);
            return 1;
        }
        return 0;
    }

    public void pauseSimulation() {
        scheduler.pause("foodCourt-OrderProcessing-Simulation");
        LOG.info("FoodCourt simulation paused");
    }

    public void resumeSimulation() {
        scheduler.resume("foodCourt-OrderProcessing-Simulation");
        LOG.info("FoodCourt simulation resumed");
    }
}