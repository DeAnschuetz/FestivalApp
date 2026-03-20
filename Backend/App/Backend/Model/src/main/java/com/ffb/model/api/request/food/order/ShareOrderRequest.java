package com.ffb.model.api.request.food.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "orderId",
        "loginNr"
})
public record ShareOrderRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID orderId,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr
) {

}
