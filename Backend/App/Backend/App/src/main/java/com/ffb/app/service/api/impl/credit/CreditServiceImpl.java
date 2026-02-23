package com.ffb.app.service.api.impl.credit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.service.api.api.credit.CreditService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    private final int INITIAL_AMMOUNT = 1000;

    private final CreditDao creditDao;

    @Inject
    public CreditServiceImpl(CreditDao creditDao) {
        this.creditDao = creditDao;
    }

    @Override
    public Credit getByLoginNr(String loginNr) throws ServiceException {
        try {
            return creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public Credit changeAmount(String loginNr, double amount) throws ServiceException {
        if (amount <= 0) {
            throw new ServiceException("amount must be > 0", Response.Status.BAD_REQUEST);
        }

        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        double oldAmount = credit.getAmount();
        double newAmount = oldAmount - amount;

        if (newAmount < 0) {
            throw new ServiceException("Insufficient credit", Response.Status.BAD_REQUEST);
        }
        credit.setAmount(newAmount);

        CreditHistory creditHistory = new CreditHistory(
                UUID.randomUUID(),
                oldAmount,
                newAmount,
                LocalDateTime.now()
        );
        creditHistory.setAccount(credit.getAccount());
        creditHistory.setCredit(credit);

        creditDao.persist(credit);
        creditDao.persistHistory(creditHistory);
        return credit;
    }

    @Override
    public List<CreditHistory> getHistoryForAccount(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditDao.findHistoryByAccountId(loginNr, pageIndex, pageSize);
    }

    @Override
    public List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditDao.findHistoryByCreditId(creditId, pageIndex, pageSize);
    }
}
