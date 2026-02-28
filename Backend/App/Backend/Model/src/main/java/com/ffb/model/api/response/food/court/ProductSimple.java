package com.ffb.model.api.response.food.court;

import java.util.UUID;

public record ProductSimple(UUID id, String name, byte icon, int count) {

}
