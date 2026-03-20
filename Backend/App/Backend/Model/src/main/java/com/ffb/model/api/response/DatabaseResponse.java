package com.ffb.model.api.response;

import com.ffb.model.api.response.account.AccountResponseFull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(requiredProperties = {
        "accounts"
})
public record DatabaseResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) List<AccountResponseFull> accounts
) {
}
