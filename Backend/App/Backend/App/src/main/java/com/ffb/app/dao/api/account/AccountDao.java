package com.ffb.app.dao.api.account;

import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.account.Ticket;
import com.ffb.model.exception.DaoException;
import java.util.List;

public interface AccountDao {

    Account findByLoginNr(String loginNr) throws DaoException;

    boolean existsByLoginNr(String loginNr);

    List<Account> getAll();

    void persist(Account account);

    Account getByLoginNr(String loginNr) throws DaoException;

    void flush();

    Ticket getTicketByLoginNr(String loginNr) throws DaoException;

    void persistTicket(Ticket ticket);

    List<Ticket> geAllTickets();

    boolean existsTicketByLoginNr(String loginNr);
}
