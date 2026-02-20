package com.ffb.app.repository.impl.credit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;

@ApplicationScoped
public class CreditRepositoryImpl implements CreditRepository {

    private final EntityManager em;

    @Inject
    public CreditRepositoryImpl(EntityManager em) {
        this.em = em;

    }

    public Optional<Credit> findByLoginNr(String loginNr) {
        return find("account.loginNr", loginNr).firstResultOptional();
    }

    public boolean existsByLoginNr(String loginNr) {
        return count("account.loginNr", loginNr) > 0;
    }

    public void persistHistory(CreditHistory history) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {
        em.persist(history);
    }

    public List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return em.createQuery(
                        "select h from CreditHistory h where h.account.loginNr = :lnr order by h.changeTime desc",
                        CreditHistory.class
                )
                .setParameter("lnr", loginNr)//
                .setFirstResult(pageIndex * pageSize)//
                .setMaxResults(pageSize)//
                .getResultList()//
        ;
    }

    public List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
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
