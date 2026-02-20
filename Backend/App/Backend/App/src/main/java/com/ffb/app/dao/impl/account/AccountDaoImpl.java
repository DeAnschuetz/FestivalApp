package com.ffb.app.dao.impl.account;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.model.db.objects.account.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccountDaoImpl implements AccountDao {

    private final AccountRepository accountRepo;

    @Inject
    public AccountDaoImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Optional<Account> findByLoginNr(String loginNr) {
        return accountRepo.getByLoginNr(loginNr);
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        return accountRepo.existsByLoginNr(loginNr);
    }

    @Override
    public List<Account> getAll() {
        return accountRepo.listAll();
    }

    @Override
    public void persist(Account account) {
        accountRepo.persist(account);
    }

    @Override
    public Optional<Account> getByLoginNr(String loginNr) {
        return accountRepo.getByLoginNr(loginNr);
    }
}
