package com.ffb.app.repository.impl.credit;

import com.ffb.app.repository.api.credit.CreditHistoryRepository;
import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import java.util.List;

@ApplicationScoped
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    @Override
    public List<CreditHistory> findHistoryByLoginNr(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return find("SELECT h " +
                        "FROM CreditHistory h " +
                        "WHERE h.account.ticket.loginNr = ?1 " +
                        "ORDER BY h.changeTime DESC",
                    loginNr
                )//
                .list()//
        ;
    }
}
