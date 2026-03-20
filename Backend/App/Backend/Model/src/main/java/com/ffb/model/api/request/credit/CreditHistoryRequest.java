package com.ffb.model.api.request.credit;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "loginNr",
        "pageIndex",
        "pageSize"
})
public record CreditHistoryRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int pageIndex,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) int pageSize
) {
}
