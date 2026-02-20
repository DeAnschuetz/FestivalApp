package com.ffb.app.repository.impl.account;

import java.util.List;
import java.util.Optional;

import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.model.db.objects.account.Account;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public Optional<Account> getByLoginNr(String loginNr) {
        return find(
                    "loginNr",
                    loginNr
                )//
                .firstResultOptional()//
        ;
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        return count("loginNr", loginNr) > 0;
    }

    @Override
    public List<Account> getAllAccounts() {
    	return Account.listAll();
    }

}
