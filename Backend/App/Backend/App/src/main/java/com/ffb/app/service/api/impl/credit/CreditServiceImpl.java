package com.ffb.app.service.api.impl.credit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.service.api.api.credit.CreditService;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
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

    private final CreditDao creditDao;

    @Inject
    public CreditServiceImpl(CreditDao creditDao) {
        this.creditDao = creditDao;
    }

    @Override
    public CreditResponse getByLoginNr(String loginNr) throws ServiceException {
        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        return getCreditResponse(credit);
    }

    @Override
    @Transactional
    public CreditResponse changeAmount(String loginNr, double amount) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            throw new ServiceException("loginNr must not be null or empty", Response.Status.BAD_REQUEST);
        }
        if (amount < 0) {
            throw new ServiceException("amount must be > 0", Response.Status.BAD_REQUEST);
        }

        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        double oldAmount = credit.getAmount();
        double newAmount = oldAmount + amount;

        if (newAmount < 0) {
            throw new ServiceException("Insufficient credit", Response.Status.INTERNAL_SERVER_ERROR);
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
        return getCreditResponse(credit);
    }

    @Override
    public List<CreditHistoryResponse> getHistoryByLoginNr(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        List<CreditHistory> creditHistory = creditDao.findHistoryByAccountId(loginNr, pageIndex, pageSize);
        return creditHistory.stream()//
                .map(this::getCreditHistoryResponse)//
                .toList()//
        ;
    }

    	/*
		Private Helper Functions
	*/

    private CreditResponse getCreditResponse(Credit credit) {
        return new CreditResponse(
                credit.getAmount()
        );
    }


    private CreditHistoryResponse getCreditHistoryResponse(CreditHistory creditHistory) {
        return new CreditHistoryResponse(
                creditHistory.getOldAmmount(),
                creditHistory.getNewAmmount(),
                creditHistory.getChangeTime()
        );
    }
}
