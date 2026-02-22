package com.ffb.app.repository.impl.account;

import com.ffb.app.repository.api.account.TicketRepository;
import com.ffb.model.db.objects.account.Ticket;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class TicketRepositoryIml implements TicketRepository {
    @Override
    public Optional<Ticket> getByTicketNr(String ticketNr) {
        return find("loginNr", ticketNr)//
                .firstResultOptional()//
        ;
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        return count("loginNr", loginNr) > 0;
    }
}
