package com.ffb.model.response.foodcourt;

import java.util.UUID;

public record ProductSimple(UUID id, String name, byte icon, int count) {

}
