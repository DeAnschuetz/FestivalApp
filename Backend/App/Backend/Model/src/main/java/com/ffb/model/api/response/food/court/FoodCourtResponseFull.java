package com.ffb.model.api.response.food.court;

import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.product.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(requiredProperties = {
        "foodCourt",
        "products",
        "orders"
})
public record FoodCourtResponseFull(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodCourtResponse foodCourt,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<ProductResponse> products,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderResponse> orders
) {
}
