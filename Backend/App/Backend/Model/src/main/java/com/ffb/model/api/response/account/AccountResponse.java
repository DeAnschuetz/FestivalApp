package com.ffb.model.api.response.account;

import com.ffb.model.db.objects.account.AccountType;

public record AccountResponse(
        String id,
        String loginNr,
        AccountType type
) {
}
