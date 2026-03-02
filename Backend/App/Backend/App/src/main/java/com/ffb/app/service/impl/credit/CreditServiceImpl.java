package com.ffb.app.service.impl.credit;

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
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    // TODO Logging fertig
    private final Logger LOG = LoggerFactory.getLogger(CreditService.class);

    private final CreditDao creditDao;
    private final ResponseMapper mapper;

    @Inject
    public CreditServiceImpl(CreditDao creditDao, ResponseMapper mapper) {
        this.creditDao = creditDao;
        this.mapper = mapper;
    }

    @Override
    public CreditResponse getByLoginNr(String loginNr) throws ServiceException {
        LOG.trace("ENTER: getByLoginNr; loginNr={{}}", loginNr);
        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not find credit for loginNr={{}}; Exception: ", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        CreditResponse response = mapper.getCreditResponse(credit);
        LOG.trace("EXIT: getByLoginNr; response=[{}]", response);
        return response;
    }

    @Override
    @Transactional
    public CreditResponse changeAmount(String loginNr, CreditAddRequest request) throws ServiceException {
        LOG.trace("ENTER: changeAmount; loginNr={{}}, request=[{}]", loginNr, request);
        double amount = request.amount();

        Credit credit;
        try {
            credit = creditDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("Could not find credit for loginNr={{}}; Exception: ", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        double oldAmount = credit.getAmount();
        double newAmount = oldAmount + amount;
        LOG.debug("Changing credit for loginNr={{}}; oldAmount={}, change={}, newAmount={}", loginNr, oldAmount, amount, newAmount);


        if (newAmount < 0) {
            LOG.error("Insufficient credit for loginNr={{}}; attempted change={}, currentAmount={}", loginNr, amount, oldAmount);
            throw new ServiceException("Insufficient credit", Response.Status.INTERNAL_SERVER_ERROR);
        }
        credit.setAmount(newAmount);
        LOG.info("Credit updated for loginNr={{}}; newAmount={}", loginNr, newAmount);
        CreditResponse response = mapper.getCreditResponse(credit);
        LOG.trace("EXIT: changeAmount; response=[{}]", response);
        return response;
    }

    @Override
    public List<CreditHistoryResponse> getHistoryByLoginNr(CreditHistoryRequest request) {
        LOG.trace("ENTER: getHistoryByLoginNr; request={}", request);
        String loginNr = request.loginNr();
        int pageIndex = request.pageIndex();
        int pageSize = request.pageSize();

        List<CreditHistory> creditHistory =
                creditDao.findHistoryByAccountId(loginNr, pageIndex, pageSize);

        List<CreditHistoryResponse> responses = creditHistory.stream()//
                .map(mapper::getCreditHistoryResponse)//
                .toList()//
        ;
        LOG.trace("EXIT: getHistoryByLoginNr; found {} entries for loginNr={{}}",
                responses.size(), loginNr);
        return responses;
    }
}
