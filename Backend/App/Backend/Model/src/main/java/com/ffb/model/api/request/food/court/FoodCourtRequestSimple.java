package com.ffb.model.api.request.food.court;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "displayName"
})
public record FoodCourtRequestSimple(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String displayName
) {
}
