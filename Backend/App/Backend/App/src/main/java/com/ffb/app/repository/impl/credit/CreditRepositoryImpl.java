package com.ffb.app.repository.impl.credit;

import java.util.Optional;
import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.model.db.objects.credit.Credit;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreditRepositoryImpl implements CreditRepository {

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
