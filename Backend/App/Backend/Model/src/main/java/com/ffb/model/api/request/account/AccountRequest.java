package com.ffb.model.api.request.account;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "loginNr",
        "password"
})
public record AccountRequest(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String password
) {
}
