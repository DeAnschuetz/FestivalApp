package com.ffb.app.service.api.food.court;

import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.image.Image;
import jakarta.persistence.EntityNotFoundException;

import java.io.PushbackInputStream;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface FoodCourtService {

    List<FoodCourt> listAll();

    FoodCourt getById(UUID id);

    FoodCourt getByLoginNr(String loginNr);

    FoodCourt create(String LoginNr, String name);

    FoodCourt updateByLoginNr(String loginNr, String name);

    FoodCourt updateById(UUID id, String loginNr, String name) throws EntityNotFoundException;

    void delete(UUID id);

    FoodCourt getWithRelations(UUID id, boolean waitingTime, boolean foodOrders);

    Image getImageByUri(URI uri);

    Image getImageByID(UUID id);

    URI addImage(String loginNr, PushbackInputStream inputData) throws EntityNotFoundException;
}
