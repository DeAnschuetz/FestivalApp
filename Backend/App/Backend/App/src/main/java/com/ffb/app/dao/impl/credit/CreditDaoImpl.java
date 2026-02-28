package com.ffb.app.dao.impl.credit;

import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.repository.api.credit.CreditHistoryRepository;
import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.model.db.object.credit.Credit;
import com.ffb.model.db.object.credit.CreditHistory;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import java.util.List;

@ApplicationScoped
public class CreditDaoImpl implements CreditDao {

    // TODO Logging

    private final CreditRepository creditRepo;
    private  final CreditHistoryRepository creditHistoryRepo;

    @Inject
    public CreditDaoImpl(CreditRepository creditRepo, CreditHistoryRepository creditHistoryRepo) {
        this.creditRepo = creditRepo;
        this.creditHistoryRepo = creditHistoryRepo;
    }

    @Override
    public Credit getByLoginNr(String loginNr) throws DaoException {
        return creditRepo.findByLoginNr(loginNr)
                .orElseThrow(() -> new DaoException("No credit found for loginNr: " + loginNr))
        ;
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        return creditRepo.existsByLoginNr(loginNr);
    }

    @Override
    public void persistHistory(CreditHistory history) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {
        creditHistoryRepo.persist(history);
    }

    @Override
    public List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditHistoryRepo.findHistoryByLoginNr(loginNr, pageIndex, pageSize);
    }

    @Override
    public void persist(Credit credit) {
        creditRepo.persist(credit);
    }
}
