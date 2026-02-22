package com.ffb.app.repository.impl.credit;

import com.ffb.app.repository.api.credit.CreditHistoryRepository;
import com.ffb.model.db.objects.credit.CreditHistory;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {


    private final EntityManager em;

    @Inject
    public CreditHistoryRepositoryImpl(EntityManager em) {
        this.em = em;

    }

    @Override
    public void persistHistory(CreditHistory history) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {
        em.persist(history);
    }

    @Override
    public List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return find("SELECT h" +
                        "FROM CreditHistory h" +
                        "WHERE h.account.ticket.loginNr = ?1" +
                        "ORDER BY h.changeTime DESC",
                    loginNr
                )//
                .list()//
        ;
    }

    @Override
    public List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return find("SELECT h" +
                        "FROM CreditHistory h" +
                        "WHERE h.credit.id = ?1" +
                        "ORDER BY h.changeTime DESC",
                    creditId
                )//
                .page(Page.of(pageIndex, pageSize))//
                .list()//
        ;

    }
}
