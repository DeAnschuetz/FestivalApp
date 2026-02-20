package com.ffb.model.api.response.foodcourt;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public record FoodcourtRequest(UUID accountId, String name, URI imageUri, int waitingTime, List<ProductSimple> products) {
}
