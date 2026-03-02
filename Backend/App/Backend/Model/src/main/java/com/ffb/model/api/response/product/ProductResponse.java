package com.ffb.model.api.response.product;

import java.util.List;
import java.util.UUID;

public record ProductResponse(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning, int productCount, List<ProductResponse> subProducts) {
}
