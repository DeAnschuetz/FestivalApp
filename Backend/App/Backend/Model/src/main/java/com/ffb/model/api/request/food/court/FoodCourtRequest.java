package com.ffb.model.api.request.food.court;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "loginNr",
        "displayName"
})
public record FoodCourtRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName
) {
}
