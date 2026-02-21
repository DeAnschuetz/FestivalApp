package com.ffb.app.dao.api.account;

import com.ffb.model.db.objects.account.Account;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    Account findByLoginNr(String loginNr) throws DaoException;

    boolean existsByLoginNr(String loginNr);

    List<Account> getAll();

    void persist(Account account);

    Account getByLoginNr(String loginNr) throws DaoException;

    void flush();
}
