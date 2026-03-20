package com.ffb.model.api.request.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredProperties = {
        "loginNr",
        "password"
})
public record RegisterRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String password
) {
}
