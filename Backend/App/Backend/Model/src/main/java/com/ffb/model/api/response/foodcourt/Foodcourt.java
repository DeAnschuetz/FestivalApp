package com.ffb.model.api.response.foodcourt;

import java.util.List;
import java.util.UUID;

public record Foodcourt(UUID id, String name, byte image, int waitingTime, List<ProductSimple> products) {

}
