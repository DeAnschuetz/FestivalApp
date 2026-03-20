package com.ffb.model.api.response.cart;

import com.ffb.model.api.response.account.AccountResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(requiredProperties = {
        "account",
        "hasPrio",
        "total",
        "cartItems"
})
public record CartResponseFull(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) AccountResponse account,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean hasPrio,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double total,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<CartItemResponse> cartItems
) {
}
