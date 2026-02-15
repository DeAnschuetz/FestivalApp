package com.ffb.model.response.foodcourt;

import java.util.List;
import java.util.UUID;

public record Foodcourt(UUID id, String name, byte image, int waitingTime, List<ProductSimple> products) {

}
