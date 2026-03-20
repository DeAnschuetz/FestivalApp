package com.ffb.model.api.request.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "mainProductId",
        "subProductId"
})
public record ProductLinkRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID mainProductId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID subProductId
) {
}
