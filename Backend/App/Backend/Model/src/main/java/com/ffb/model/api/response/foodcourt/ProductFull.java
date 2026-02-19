package com.ffb.model.response.foodcourt;

import java.util.List;
import java.util.UUID;

public record ProductFull(UUID id, String name, byte icon, double price, List<SubProductFull> subProducts) {

}
