package com.ffb.app.service.api.credit;

import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.credit.CreditHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditService {

    Credit createInitialCredit(Account account);

    Credit getByAccountId(UUID accountId);

    Credit addAmount(UUID accountId, double delta);

    Credit subtractAmount(UUID accountId, double delta);

    List<CreditHistory> getHistoryForAccount(UUID accountId, int pageIndex, int pageSize);

    List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize);

    Credit findByLoginNr(String loginNr);
}
