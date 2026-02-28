package com.ffb.app.service.impl.credit;

import java.util.List;

import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.app.service.api.credit.CreditService;
import com.ffb.model.api.request.credit.CreditAddRequest;
import com.ffb.model.api.request.credit.CreditHistoryRequest;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.db.object.credit.Credit;
import com.ffb.model.db.object.credit.CreditHistory;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    // TODO Logging

    private final CreditDao creditDao;
    private final ResponseMapper mapper;

    @Inject
    public CreditServiceImpl(CreditDao creditDao, ResponseMapper mapper) {
        this.creditDao = creditDao;
        this.mapper = mapper;
    }

    @Override
    public CreditResponse getByLoginNr(String loginNr) throws ServiceException {
        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        return mapper.getCreditResponse(credit);
    }

    @Override
    @Transactional
    public CreditResponse changeAmount(String loginNr, CreditAddRequest request) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            throw new ServiceException("loginNr must not be null or empty", Response.Status.BAD_REQUEST);
        }
        double amount = request.amount();
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
        return mapper.getCreditResponse(credit);
    }

    @Override
    public List<CreditHistoryResponse> getHistoryByLoginNr(CreditHistoryRequest request) {
        String loginNr = request.loginNr();
        int pageIndex = request.pageIndex();
        int pageSize = request.pageSize();

        List<CreditHistory> creditHistory = creditDao.findHistoryByAccountId(loginNr, pageIndex, pageSize);
        return creditHistory.stream()//
                .map(mapper::getCreditHistoryResponse)//
                .toList()//
        ;
    }
}
