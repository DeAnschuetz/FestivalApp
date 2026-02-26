package com.ffb.app.service.api.api.token;

import java.util.Set;

public interface TokenService {

    String createToken(String loginNr, Set<String> roles);
}
