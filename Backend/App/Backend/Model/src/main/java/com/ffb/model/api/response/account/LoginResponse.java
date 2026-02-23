package com.ffb.model.api.response.account;

import com.ffb.model.db.objects.account.AccountType;

public record LoginResponse(
        AccountType role
) {}