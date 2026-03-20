package com.ffb.model.api.response.credit;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "amount",
        "history"
})
public record CreditResponseFull(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double amount,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<CreditHistoryResponse> history
) {
}
