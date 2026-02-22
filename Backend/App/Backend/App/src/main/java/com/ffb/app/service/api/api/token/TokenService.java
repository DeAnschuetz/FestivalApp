package com.ffb.app.service.api.api.token;

import com.ffb.model.db.objects.account.AccountType;

import java.util.Set;

public interface TokenService {

    String createToken(String loginNr, Set<String> roles);
}
