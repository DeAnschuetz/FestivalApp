package com.ffb.model.api.request.product;

import java.util.UUID;

public record ProductLinkRequest(UUID mainProductId, UUID subProductId) {
}
