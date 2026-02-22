package com.ffb.app.dao.impl.credit;

import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.repository.api.credit.CreditHistoryRepository;
import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CreditDaoImpl implements CreditDao {

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
        creditHistoryRepo.persistHistory(history);
    }

    @Override
    public List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditHistoryRepo.findHistoryByAccountId(loginNr, pageIndex, pageSize);
    }

    @Override
    public List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditHistoryRepo.findHistoryByCreditId(creditId, pageIndex, pageSize);
    }

    @Override
    public void persist(Credit credit) {
        creditRepo.persist(credit);
    }
}
