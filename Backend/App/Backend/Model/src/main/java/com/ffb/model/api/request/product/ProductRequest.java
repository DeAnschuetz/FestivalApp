package com.ffb.model.api.request.product;

import java.util.UUID;

public record ProductRequest(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
}
