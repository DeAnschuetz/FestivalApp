package com.ffb.app.dao.impl.account;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.app.repository.api.account.TicketRepository;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.account.Ticket;
import com.ffb.model.exception.DaoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@ApplicationScoped
public class AccountDaoImpl implements AccountDao {

    private final Logger LOG = LoggerFactory.getLogger(AccountDaoImpl.class);

    private final AccountRepository accountRepo;
    private final TicketRepository ticketRepo;

    @Inject
    public AccountDaoImpl(AccountRepository accountRepo, TicketRepository ticketRepo) {
        this.accountRepo = accountRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public Account getByLoginNr(String loginNr) throws DaoException {
        LOG.trace("ENTER: findByLoginNr; loginNr={{}}", loginNr);
        Account account = accountRepo.getByLoginNr(loginNr)//
                .orElseThrow(() -> {
                    LOG.error("account not found for loginNr: {{}}", loginNr);
                    return new DaoException("Account not found for loginNr={" + loginNr + "}");
                })//
        ;
        LOG.trace("EXIT: findByLoginNr; account={{}}", account);
        return account;
    }

    @Override
    public boolean existsByLoginNr(String loginNr) {
        LOG.trace("ENTER: existsByLoginNr; loginNr={{}}", loginNr);
        boolean exists = accountRepo.existsByLoginNr(loginNr);
        LOG.trace("EXIT: existsByLoginNr; exists={{}}", exists);
        return exists;
    }

    @Override
    public List<Account> listAll() {
        LOG.trace("ENTER: getAll;");
        List<Account> result =  accountRepo.listAll();
        LOG.trace("EXIT: getAll found {}", result.size());
        return result;
    }

    @Override
    public void persist(Account account) {
        accountRepo.persist(account);
    }

    @Override
    public Ticket getTicketByLoginNr(String loginNr) throws DaoException {
        LOG.trace("ENTER: getTicketByLoginNr; loginNr={{}}", loginNr);
        Ticket ticket = ticketRepo.getByLoginNr(loginNr)//
                .orElseThrow(() -> {
                    LOG.error("could not find ticket for loginNr={{}}", loginNr);
                    return new DaoException("Ticket not found for loginNr={" + loginNr + "}");
                })//
        ;
        LOG.trace("EXIT: getTicketByLoginNr; ticket={{}}", ticket);
        return ticket;
    }

    @Override
    public List<Ticket> geAllTickets() {
        LOG.trace("ENTER: getAllTickets;");
        List<Ticket> tickets = ticketRepo.listAll();
        LOG.trace("EXIT: getAllTickets found {}", tickets.size());
        return tickets;
    }

    @Override
    public boolean existsTicketByLoginNr(String loginNr) {
        LOG.trace("ENTER: existsTicketByLoginNr; loginNr={{}}", loginNr);
        boolean exists  = ticketRepo.existsByLoginNr(loginNr);
        LOG.trace("EXIT: existsTicketByLoginNr; exists={{}}", exists);
        return exists;
    }

    @Override
    public void persistTicket(Ticket ticket) {
        ticketRepo.persist(ticket);
    }
}
