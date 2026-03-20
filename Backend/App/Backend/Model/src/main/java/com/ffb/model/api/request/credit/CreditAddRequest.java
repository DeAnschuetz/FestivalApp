package com.ffb.model.api.request.credit;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "amount"
})
public record CreditAddRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double amount
) {
}
