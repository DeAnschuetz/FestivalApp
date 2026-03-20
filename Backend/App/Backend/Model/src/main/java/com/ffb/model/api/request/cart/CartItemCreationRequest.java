package com.ffb.model.api.request.cart;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "productId",
        "itemCount",
        "extra"
})
public record CartItemCreationRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID productId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int itemCount,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String extra
) {
}
