package com.ffb.app.repository.api.account;

import com.ffb.model.db.object.account.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends PanacheRepositoryBase<Ticket, UUID> {

    Optional<Ticket> getByLoginNr(String ticketNr);

    boolean existsByLoginNr(String loginNr);
}
