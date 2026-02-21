package com.ffb.app.dao.impl.account;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.exception.DaoException;
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
    public Account findByLoginNr(String loginNr) throws DaoException {
        return accountRepo.getByLoginNr(loginNr)//
                .orElseThrow(() -> new DaoException("Account not found for loginNr=" + loginNr))//
        ;
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
    public Account getByLoginNr(String loginNr) throws DaoException {
        return accountRepo.getByLoginNr(loginNr)//
                .orElseThrow(() -> new DaoException("Account not found for loginNr=" + loginNr))//
        ;
    }

    @Override
    public void flush() {
        accountRepo.flush();
    }
}
