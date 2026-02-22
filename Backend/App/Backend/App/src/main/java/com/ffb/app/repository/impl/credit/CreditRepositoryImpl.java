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

    @Override
    public Optional<Credit> findByLoginNr(String loginNr) {
        return find(
                    "account.ticket.loginNr",
                    loginNr
                )//
                .firstResultOptional()
        ;
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        return count("account.ticket.loginNr",loginNr) > 0;
    }
}
