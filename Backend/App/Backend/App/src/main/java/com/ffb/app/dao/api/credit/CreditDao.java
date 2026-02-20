package com.ffb.app.dao.api.credit;

import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditDao {

    Optional<Credit> findByLoginNr(String loginNr);

    boolean existsByLoginNr(String loginNr);

    void persistHistory(CreditHistory history) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;

    List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    void persist(Credit credit);
}
