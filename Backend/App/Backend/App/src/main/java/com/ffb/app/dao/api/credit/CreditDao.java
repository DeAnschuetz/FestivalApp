package com.ffb.app.dao.api.credit;

import com.ffb.model.db.object.credit.Credit;
import com.ffb.model.db.object.credit.CreditHistory;
import com.ffb.model.exception.DaoException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;

public interface CreditDao {

    Credit getByLoginNr(String loginNr) throws DaoException;

    boolean existsByLoginNr(String loginNr);

    void persistHistory(CreditHistory history) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;

    List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    void persist(Credit credit);
}
