package com.ffb.model.api.response.food_court;

import java.util.List;
import java.util.UUID;

public record ProductFull(UUID id, String name, byte icon, double price, List<SubProductFull> subProducts) {

}
