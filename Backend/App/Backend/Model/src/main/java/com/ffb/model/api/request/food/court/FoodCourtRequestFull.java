package com.ffb.model.api.request.food.court;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "loginNr",
        "name"
})
public record FoodCourtRequestFull(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String name
) {
}
