package com.ffb.model.api.response.account;

import com.ffb.model.db.object.account.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(requiredProperties = {
        "id",
        "loginNr",
        "type"
})
public record AccountResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) UUID id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String loginNr,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) AccountType type
) {
}
