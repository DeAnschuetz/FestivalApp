package com.ffb.model.api.response.credit;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "credit"
})
public record CreditResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) double credit
) {

}
