package com.ffb.model.api.request.product;

import java.util.UUID;

public record MainSubLinkCreateRequest(UUID mainProductId, UUID subProductId) {
}
