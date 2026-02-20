package com.ffb.app.dao.api.account;

import com.ffb.model.db.objects.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    Optional<Account> findByLoginNr(String loginNr);

    boolean existsByLoginNr(String loginNr);

    List<Account> getAll();

    void persist(Account account);

    Optional<Account> getByLoginNr(String loginNr);
}
