package com.ffb.app.service.api.api.credit;

import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;
import com.ffb.model.exception.ServiceException;

import java.math.BigDecimal;
import java.util.List;

public interface CreditService {

    CreditResponse getByLoginNr(String loginNr) throws ServiceException;

    CreditResponse changeAmount(String loginNr, double amount) throws ServiceException;

    List<CreditHistoryResponse> getHistoryByLoginNr(String loginNr, int pageIndex, int pageSize) throws ServiceException;

}
