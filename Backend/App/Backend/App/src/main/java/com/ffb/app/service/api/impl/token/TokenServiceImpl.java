package com.ffb.app.service.api.impl.token;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.service.api.api.token.TokenService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class TokenServiceImpl implements TokenService {

    private final AccountDao accountDao;

    public TokenServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public String createToken(String loginNr, Set<String> roles) {
        return Jwt.issuer("https://your-app.example")
                .upn(loginNr)//
                .groups(roles)//
                .expiresIn(Duration.ofHours(2))//
                .sign()//
        ;
    }
}
