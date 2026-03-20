package com.ffb.model.api.response.credit;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(requiredProperties = {
        "oldAmount",
        "newAmount",
        "changeTime"
})
public record CreditHistoryResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double oldAmount,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double newAmount,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) LocalDateTime changeTime
) {
}
