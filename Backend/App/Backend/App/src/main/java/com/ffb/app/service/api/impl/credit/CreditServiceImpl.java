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

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    private final int INITIAL_AMMOUNT = 1000;

    private final CreditDao creditDao;

    @Inject
    public CreditServiceImpl(CreditDao creditDao) {
        this.creditDao = creditDao;
    }

    @Override
    @Transactional
    public void createInitialCredit(Account account) throws ServiceException {
        if (account == null) throw new IllegalArgumentException("account must not be null");

        String loginNr = account.getLoginNr();
        if (creditDao.existsByLoginNr(loginNr)) {
            throw new ServiceException("Credit already exists for loginNr=" + loginNr);
        }

        Credit credit = new Credit(UUID.randomUUID(), INITIAL_AMMOUNT, account);
        creditDao.persist(credit);

        CreditHistory creditHistory = new CreditHistory(
                UUID.randomUUID(),
                0.0,
                INITIAL_AMMOUNT,
                LocalDateTime.now()
        );
        creditHistory.setAccount(account);
        creditHistory.setCredit(credit);
        creditDao.persistHistory(creditHistory);

    }

    @Override
    public Credit getByLoginNr(String loginNr) throws ServiceException {
        try {
            return creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional
    public Credit changeAmount(String loginNr, double amount) throws ServiceException {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        double oldAmount = credit.getAmount();
        double newAmount = oldAmount - amount;

        if (newAmount < 0) {
            throw new ServiceException("Insufficient credit");
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
