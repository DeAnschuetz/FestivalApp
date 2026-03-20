package com.ffb.model.api.response.account;

import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.api.response.credit.CreditResponseFull;
import com.ffb.model.api.response.food.court.FoodCourtResponseFull;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponseFull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(requiredProperties = {
        "account",
        "credit",
        "cart",
        "orders",
        "foodCourt"
})
public record AccountResponseFull(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) AccountResponse account,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) CreditResponseFull credit,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) CartResponseSimple cart,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<FoodOrderResponseFull> orders,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) FoodCourtResponseFull foodCourt
) {
}
