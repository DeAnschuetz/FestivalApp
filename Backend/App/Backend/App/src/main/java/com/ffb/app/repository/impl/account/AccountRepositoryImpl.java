package com.ffb.app.repository.impl.account;

import java.util.List;
import java.util.Optional;

import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.model.db.objects.account.Account;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByLoginNr(String loginNr) {
        return find(
                    "loginNr",
                    loginNr
                )//
                .firstResultOptional()//
        ;
    }

    public boolean existsByLoginNr(String loginNr) {
        return count("loginNr", loginNr) > 0;
    }
    
    public List<Account> getAllAccounts() {
    	return Account.listAll();
    }
}
