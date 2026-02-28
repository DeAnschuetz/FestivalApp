package com.ffb.model.api.response.account;

import com.ffb.model.db.object.account.AccountType;

public record LoginResponse(
        AccountType role
) {}