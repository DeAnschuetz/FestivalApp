package com.ffb.model.api.request.cart;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "cartItemId",
        "itemCount",
        "extra"
})
public record CartItemUpdateRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID cartItemId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int itemCount,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String extra
) {
}
