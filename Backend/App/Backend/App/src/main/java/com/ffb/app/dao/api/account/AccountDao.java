package com.ffb.app.dao.api.account;

import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.account.Ticket;
import com.ffb.model.exception.DaoException;

import java.util.List;

public interface AccountDao {

    Account getByLoginNr(String loginNr) throws DaoException;

    boolean existsByLoginNr(String loginNr);

    List<Account> listAll();

    void persist(Account account);

    Ticket getTicketByLoginNr(String loginNr) throws DaoException;

    List<Ticket> geAllTickets();

    boolean existsTicketByLoginNr(String loginNr);

    void persistTicket(Ticket ticket);
}
