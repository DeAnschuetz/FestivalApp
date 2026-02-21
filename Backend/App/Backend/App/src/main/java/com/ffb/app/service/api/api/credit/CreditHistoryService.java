package com.ffb.app.service.api.api.credit;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;

public interface CreditHistoryService {

	public CreditHistory recordChange(Account account, Credit credit, double oldAmount, double newAmount);

	public List<CreditHistory> getHistoryForAccount(UUID accountId, int pageIndex, int pageSize);

	public List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize);
}
