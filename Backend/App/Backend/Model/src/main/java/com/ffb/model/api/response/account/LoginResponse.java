package com.ffb.model.api.response.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "logiNr",
        "token"
})
public record LoginResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String token
) {}