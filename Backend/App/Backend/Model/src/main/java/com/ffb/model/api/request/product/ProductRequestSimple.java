package com.ffb.model.api.request.product;

public record ProductRequestSimple(double price, String displayName, String symbolIdentifier, int minimalWarning) {
}
