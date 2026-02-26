package com.ffb.model.api.response.account;

import com.ffb.model.db.objects.account.AccountType;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        String loginNr,
        AccountType type
) {
}
