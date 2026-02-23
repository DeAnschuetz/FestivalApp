package com.ffb.app.service.api.api.credit;

import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;
import com.ffb.model.exception.ServiceException;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.UUID;

public interface CreditService {

    Credit getByLoginNr(String loginNr) throws ServiceException;

    Credit changeAmount(String loginNr, double amount) throws ServiceException;

    List<CreditHistory> getHistoryForAccount(String loginNr, int pageIndex, int pageSize) throws ServiceException;

    List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize) throws ServiceException;

}
