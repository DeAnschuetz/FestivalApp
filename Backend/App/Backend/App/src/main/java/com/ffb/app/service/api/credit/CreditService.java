package com.ffb.app.service.api.credit;

import com.ffb.model.api.request.credit.CreditAddRequest;
import com.ffb.model.api.request.credit.CreditHistoryRequest;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.exception.ServiceException;

import java.util.List;

public interface CreditService {

    CreditResponse getByLoginNr(String loginNr) throws ServiceException;

    CreditResponse changeAmount(String loginNr, CreditAddRequest request) throws ServiceException;

    List<CreditHistoryResponse> getHistoryByLoginNr(CreditHistoryRequest request);

}
