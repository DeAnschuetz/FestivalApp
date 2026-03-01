package com.ffb.app.service.impl.food.court;

import com.ffb.model.db.object.foodorder.FoodOrderStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.food.order.FoodOrderDao;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.db.object.foodorder.FoodOrder;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FoodCourtWaitingTimeService {

    private static final Logger LOG = LoggerFactory.getLogger(FoodCourtWaitingTimeService.class);


    @ConfigProperty(name = "foodcourt.preparation.speed")
    int FOOD_PREPARATION_SPEED;

    private final FoodOrderDao foodOrderDao;
    private final FoodCourtDao foodCourtDao;

    @Inject
    public FoodCourtWaitingTimeService(FoodOrderDao foodOrderDao, FoodCourtDao foodCourtDao) {
        this.foodOrderDao = foodOrderDao;
        this.foodCourtDao = foodCourtDao;
    }


    @Scheduled(every = "{foodcourt.preparation.time}", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    @Transactional
    void updateWaitTimes() {
        LOG.info("Updating Waiting Times");
        List<FoodCourt> foodCourts = foodCourtDao.listAll();
        foodCourts.forEach(this::updateWaitingTime);
    }

    private void updateWaitingTime(FoodCourt foodCourt) {
        LOG.debug("Updating Waiting Time for '{}'", foodCourt.getDisplayName());
        UUID foodCourtId = foodCourt.getId();
        List<FoodOrder> foodOrders = foodOrderDao.listByFoodCourtId(foodCourtId);
        foodOrders = foodOrders.stream()//
                .filter(foodOrder -> foodOrder.getStatus() == FoodOrderStatus.ORDERED || foodOrder.getStatus() == FoodOrderStatus.IN_PROGRESS)//
                .toList()//
        ;
        int newWaitingTime = foodOrders.size() * FOOD_PREPARATION_SPEED;
        LOG.debug("New Waiting Time: {}", newWaitingTime);
        foodCourt.updateWaitingTime(newWaitingTime);
    }

}
