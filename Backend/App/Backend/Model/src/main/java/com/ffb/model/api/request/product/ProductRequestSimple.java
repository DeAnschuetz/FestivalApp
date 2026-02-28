package com.ffb.model.api.request.product;

import java.math.BigDecimal;

public record ProductRequestSimple(double price, String displayName, String symbolIdentifier, int minimalWarning) {
}
