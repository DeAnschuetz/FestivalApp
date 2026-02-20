package com.ffb.app.service.api.credit;

import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.UUID;

public interface CreditService {

    void createInitialCredit(Account account) throws IllegalStateException;

    Credit getByLoginNr(String loginNr) throws IllegalArgumentException;

    Credit changeAmount(String loginNr, double amount) throws IllegalStateException;

    List<CreditHistory> getHistoryForAccount(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;

}
