package com.ffb.app.service.api.impl.account;



import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.credit.CreditDao;
import com.ffb.app.service.api.api.account.AccountService;
import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.account.Ticket;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.credit.Credit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

	private final Logger LOG = Logger.getLogger(AccountServiceImpl.class);


	@ConfigProperty(name = "account.initial.credit")
	int INITIAL_CREDIT;

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
	public AccountResponse getAccountByLoginNr(String loginNr) throws ServiceException {
		LOG.trace("ENTER: getAccountByLoginNr");
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("loginNr is null or empty");
			throw new ServiceException("loginNr must not be blank", Response.Status.BAD_REQUEST);
		}
		LOG.trace("EXIT: getAccountByLoginNr");
		Account account;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
		return getAccountResponse(account);
    }

	@Override
	@Transactional
	public AccountResponse createAccount(String loginNr, String rawPassword) throws ServiceException {
		LOG.trace("ENTER: createAccount");
		if (loginNr == null || loginNr.isBlank()) {
			LOG.error("loginNr is null or empty");
			throw new ServiceException("loginNr must not be blank.", Response.Status.BAD_REQUEST);
		}
		if (rawPassword == null || rawPassword.isBlank()) {
			LOG.error("rawPassword is null or empty");
			throw new ServiceException("password must not be blank.", Response.Status.BAD_REQUEST);
		}

		if (accountDao.existsByLoginNr(loginNr)) {
			LOG.error("loginNr already exists");
			throw new ServiceException("loginNr already exists.", Response.Status.BAD_REQUEST);
		}
		Ticket ticket;
        try {
            ticket = accountDao.getTicketByLoginNr(loginNr) ;
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        AccountType type = getAccountTypeFromLoginNr(loginNr);

		UUID id = UUID.randomUUID();
		String hashedPassword = BCrypt.withDefaults().hashToString(12, rawPassword.trim().toCharArray());

		Account account = new Account(id, ticket, hashedPassword, type);
		accountDao.persist(account);
		if(type == AccountType.GUEST) {
			Credit credit = new Credit(UUID.randomUUID(), INITIAL_CREDIT, account);
			creditDao.persist(credit);
			Cart cart = new Cart(UUID.randomUUID(), false,  0, account);
			cartDao.persist(cart);
		}

		accountDao.flush();

		LOG.trace("EXIT: createAccount");
		return getAccountResponse(account);
	}

	@Override
	public AccountType verifyAccount(String loginNr, String rawPassword) throws ServiceException {
		LOG.trace("ENTER: verifyAccount");
		if (loginNr == null || rawPassword == null) {
			LOG.error("loginNr is null or empty");
			throw new ServiceException("loginNr and password must not be null.", Response.Status.BAD_REQUEST);
		}

        Account account = null;
        try {
            account = accountDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
		String storedPassword = account.getPassword();
        AccountType type = account.getType();
		boolean verified =  BCrypt.verifyer()//
				.verify(rawPassword.toCharArray(), storedPassword)//
				.verified//
		;

		LOG.trace("EXIT: verifyAccount: " + verified);
		return verified ? type : null;
	}

	@Override
	public List<AccountResponse> getAllAccounts() {
		return accountDao.getAll()//
				.stream()//
				.map(this::getAccountResponse)//
				.toList()//
		;
	}

	@Override
	@Transactional
	public List<TicketResponse> createTicket(List<String> loginNrs) throws ServiceException {
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
				.map(this::getTicketResponse)//
				.toList()//
		;
	}

	@Override
	public List<TicketResponse> getAllTickets() {
		return accountDao.geAllTickets()
				.stream()//
				.map(this::getTicketResponse)//
				.toList()//
		;
	}

	private AccountType getAccountTypeFromLoginNr(String loginNr) throws ServiceException {
		if(loginNr.startsWith("V")) {
			return AccountType.GUEST;
		} else if (loginNr.startsWith("F")) {
			return AccountType.FOOD_COURT_WORKER;
		} else if (loginNr.startsWith("A")) {
			return AccountType.ADMIN;
		} else {
			throw new ServiceException("Invalid loginNr format. Must start with 'V', 'F', or 'A'.", Response.Status.BAD_REQUEST);
		}
	}

	/*
		Private Helper Functions
	 */

	private AccountResponse getAccountResponse(Account account) {
		return new AccountResponse(
				account.getId(),
				account.getLoginNr(),
				account.getType()
		);
	}

	private TicketResponse getTicketResponse(Ticket ticket) {
		return new TicketResponse(
				ticket.getId(),
				ticket.getLoginNr()
		);
	}
	
}
