package com.ffb.model.api.response.cart;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(requiredProperties = {
        "hasPrio",
        "total",
        "cartItems"
})
public record CartResponseSimple(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) boolean hasPrio,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double total,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<CartItemResponse> cartItems
) {
	
	
}
