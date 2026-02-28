package com.ffb.app.repository.impl.credit;

import com.ffb.app.repository.api.credit.CreditHistoryRepository;
import com.ffb.model.db.object.credit.CreditHistory;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    // TODO Logging

    @Override
    public List<CreditHistory> findHistoryByLoginNr(String loginNr, int pageIndex, int pageSize) {
        return find("SELECT h " +
                        "FROM CreditHistory h " +
                        "WHERE h.credit.account.ticket.loginNr = ?1 " +
                        "ORDER BY h.changeTime DESC",
                    loginNr
                )//
                .list()//
        ;
    }
}
