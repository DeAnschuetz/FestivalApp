package com.ffb.app.repository.impl.account;

import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.model.db.object.account.Account;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class AccountRepositoryImpl implements AccountRepository {

    // TODO Logging

    @Override
    public Optional<Account> getByLoginNr(String loginNr) {
        return find(
                    "ticket.loginNr",
                    loginNr
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        return count("ticket.loginNr", loginNr) > 0;
    }
}
