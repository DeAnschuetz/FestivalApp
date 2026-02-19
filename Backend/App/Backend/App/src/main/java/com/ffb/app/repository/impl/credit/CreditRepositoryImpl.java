package com.ffb.app.repository.impl.credit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class CreditRepositoryImpl implements CreditRepository {

    @Inject
    EntityManager em;

    public Optional<Credit> findByAccountId(UUID accountId) {
        return find("account.id", accountId).firstResultOptional();
    }

    public Optional<Credit> findByLoginNr(String loginNr) {
        return find("account.loginNr", loginNr).firstResultOptional();
    }

    public boolean existsByAccountId(UUID accountId) {
        return count("account.id", accountId) > 0;
    }

    public void persistHistory(CreditHistory history) {
        em.persist(history);
    }

    public List<CreditHistory> findHistoryByAccountId(UUID accountId, int pageIndex, int pageSize) {
        return em.createQuery(
                        "select h from CreditHistory h where h.account.id = :aid order by h.changeTime desc",
                        CreditHistory.class
                )
                .setParameter("aid", accountId)//
                .setFirstResult(pageIndex * pageSize)//
                .setMaxResults(pageSize)//
                .getResultList()//
        ;
    }

    public List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize) {
        return em.createQuery(
                        "select h from CreditHistory h where h.credit.id = :cid order by h.changeTime desc",
                        CreditHistory.class
                )//
                .setParameter("cid", creditId)//
                .setFirstResult(pageIndex * pageSize)//
                .setMaxResults(pageSize)//
                .getResultList()//
        ;
    }
}
