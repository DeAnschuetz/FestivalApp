package com.ffb.model.api.response.product;

import java.util.UUID;

public record ProductResponse(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning, UUID foodCourtId) {
}
