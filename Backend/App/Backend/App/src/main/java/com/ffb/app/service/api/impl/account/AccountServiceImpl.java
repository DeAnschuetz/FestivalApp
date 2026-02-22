package com.ffb.app.service.api.impl.account;



import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.credit.CreditDao;

import com.ffb.app.service.api.api.account.AccountService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.account.Ticket;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import org.jboss.logging.Logger;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.credit.Credit;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

	private final Logger LOG = Logger.getLogger(AccountServiceImpl.class);

	private final AccountDao accountDao;
	private final CreditDao creditDao;
	private final CartDao cartDao;

	@Inject
	public AccountServiceImpl(AccountDao accountDao, CreditDao creditDao, CartDao cartDao) {
		this.accountDao = accountDao;
		this.creditDao = creditDao;
        this.cartDao = cartDao;
    }

	@Override
	public Account getAccountByLoginNr(String loginNr) throws ServiceException {
		LOG.trace("ENTER: getAccountByLoginNr");
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("loginNr is null or empty");
			throw new IllegalArgumentException("loginNr must not be blank");
		}
		LOG.trace("EXIT: getAccountByLoginNr");
        try {
            return accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

	@Override
	@Transactional
	public Account createAccount(String loginNr, String rawPassword) throws ServiceException {
		LOG.trace("ENTER: createAccount");
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("loginNr is null or empty");
			throw new ServiceException("loginNr must not be blank");
		}
		if (rawPassword == null || rawPassword.isBlank()) {
			LOG.error("rawPassword is null or empty");
			throw new ServiceException("password must not be blank");
		}

		if (accountDao.existsByLoginNr(loginNr)) {
			LOG.error("loginNr already exists");
			throw new ServiceException("loginNr already exists");
		}
		Ticket ticket;
        try {
            ticket = accountDao.getTicketByLoginNr(loginNr) ;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        AccountType type = getAccountTypeFromLoginNr(loginNr);

		UUID id = UUID.randomUUID();
		String hashedPassword = BcryptUtil.bcryptHash(rawPassword.trim());

		Account account = new Account(id, ticket, hashedPassword, type);
		Credit credit = new Credit(UUID.randomUUID(), 1000, account);
		Cart cart = new Cart(UUID.randomUUID(), false, 0, account);

		accountDao.persist(account);
		creditDao.persist(credit);
		cartDao.persist(cart);

		accountDao.flush();

		LOG.trace("EXIT: createAccount");
		return account;
	}

	@Override
	public AccountType verifyAccount(String loginNr, String rawPassword) throws ServiceException {
		LOG.trace("ENTER: verifyAccount");
		if (loginNr == null || rawPassword == null) {
			LOG.error("loginNr is null or empty");
			throw new ServiceException("loginNr and password must not be null");
		}

        Account account = null;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        AccountType type = account.getType();
		boolean verified = BcryptUtil.matches(rawPassword.trim(), account.getPassword());

		LOG.trace("EXIT: verifyAccount: " + verified);
		return verified ? type : null;
	}

	@Override
	public List<Account> getAllAccounts() {
		return accountDao.getAll();
	}

	@Override
	@Transactional
	public List<Ticket> createTicket(List<String> loginNrs) throws ServiceException {
		return loginNrs.stream()//
				.map(loginNr -> {
					boolean exists = accountDao.existsTicketByLoginNr(loginNr);
					if(exists) {
						return null;
					}
					Ticket ticket = new Ticket(UUID.randomUUID(), loginNr);
					accountDao.persistTicket(ticket);
					return ticket;
					}
				)
				.toList()//
		;
	}

	@Override
	public List<Ticket> getAllTickets() {
		return accountDao.geAllTickets();
	}

	private AccountType getAccountTypeFromLoginNr(String loginNr) throws ServiceException {
		if(loginNr.startsWith("V")) {
			return AccountType.GUEST;
		} else if (loginNr.startsWith("F")) {
			return AccountType.FOOD_COURT_WORKER;
		} else if (loginNr.startsWith("A")) {
			return AccountType.ADMIN;
		} else {
			throw new ServiceException("Invalid loginNr format. Must start with 'V', 'F', or 'A'.");
		}
	}
	
}
