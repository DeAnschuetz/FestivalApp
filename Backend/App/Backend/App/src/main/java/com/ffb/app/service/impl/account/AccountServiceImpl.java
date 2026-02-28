package com.ffb.app.service.impl.account;

import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.app.service.api.account.AccountService;
import com.ffb.model.api.request.account.AccountRequest;
import com.ffb.model.api.request.account.LoginRequest;
import com.ffb.model.api.request.account.RegisterRequest;
import com.ffb.model.api.request.ticket.TicketRequest;
import com.ffb.model.api.response.DatabaseResponse;
import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.account.AccountResponseFull;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.account.AccountType;
import com.ffb.model.db.object.account.Ticket;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.db.object.credit.Credit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import at.favre.lib.crypto.bcrypt.BCrypt;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

	// TODO Logging
	private final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

	@ConfigProperty(name = "account.initial.credit")
	int INITIAL_CREDIT;

	private final AccountDao accountDao;
	private final ResponseMapper responseMapper;

	@Inject
	public AccountServiceImpl(AccountDao accountDao, ResponseMapper responseMapper) {
		this.accountDao = accountDao;
        this.responseMapper = responseMapper;
    }

	@Override
	@Transactional
	public AccountResponse createAccount(RegisterRequest request) throws ServiceException {
		LOG.trace("ENTER: createAccount; request={}", request);
		String loginNr = request.loginNr();
		String rawPassword = request.password();
		UUID id = UUID.randomUUID();
		AccountResponse account = createAccount(loginNr, rawPassword, id);
		LOG.trace("EXIT: createAccount; {{}}", loginNr);
		return account;
	}

	@Override
	public String verifyAccount(LoginRequest request) throws ServiceException {
		LOG.trace("ENTER: verifyAccount; request={{}}", request);
		String loginNr = request.loginNr();
		String rawPassword = request.password();

        Account account;
        try {
            account = accountDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
			LOG.error("could not find account for {{}}; Exception:", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
		String storedPassword = account.getPassword();
        AccountType type = account.getType();
		boolean verified =  BCrypt.verifyer()//
				.verify(rawPassword.toCharArray(), storedPassword)//
				.verified//
		;
		if (!verified) {
			LOG.error("could not verify account {{}}", loginNr);
			throw new ServiceException("Could not verify account {" + loginNr + "}", Response.Status.BAD_REQUEST);
		}
		Set<String> roles = Set.of(type.toString());
		String token = createToken(loginNr, roles);
		LOG.trace("EXIT: verifyAccount for {{}}", loginNr);
		return token;
	}

	@Override
	public List<AccountResponse> getAllAccounts() {
		LOG.trace("ENTER: getAllAccounts");
		List<AccountResponse> accounts = accountDao.listAll()//
				.stream()//
				.map(responseMapper::getAccountResponse)//
				.toList()//
		;
		LOG.trace("EXIT: getAllAccounts found {} accounts", accounts.size());
		return accounts;
	}

	@Override
	@Transactional
	public List<TicketResponse> createTickets(TicketRequest request) {
		LOG.trace("ENTER: createTickets; request={{}}", request);
		List<String> loginNrs = request.loginNrs();
		List<TicketResponse> created = loginNrs.stream()//
                .map(loginNr -> {
                            boolean exists = accountDao.existsTicketByLoginNr(loginNr);
                            if (exists) {
								LOG.warn("loginNr {{}} already exists", loginNr);
                                return null;
                            }
							Ticket ticket = new Ticket(UUID.randomUUID(), loginNr);
							accountDao.persistTicket(ticket);
							return ticket;
                        }
                )//
				.filter(Objects::nonNull)
				.map(responseMapper::getTicketResponse)//
				.toList()//
		;
		LOG.trace("EXIT: createTickets found {} tickets", created.size());
		return created;
	}

	@Override
	public List<TicketResponse> getAllTickets() {
		LOG.trace("ENTER: getAllTickets");
		List<TicketResponse> tickets = accountDao.geAllTickets()
				.stream()//
				.map(responseMapper::getTicketResponse)//
				.toList()//
		;
		LOG.trace("EXIT: getAllTickets found {} tickets", tickets.size());
		return tickets;
	}

	@Override
	@Transactional
	public List<AccountResponse> createAccounts(List<AccountRequest> requests) {
		LOG.trace("ENTER: createAccounts; request={{}}", requests);
		List<AccountResponse> created = requests.stream()//
				.map(request -> {
                    try {
                        return createAccount(request);
                    } catch (ServiceException e) {
						LOG.error("could not create account: request={{}} Exception: ", request, e);
						return null;
                    }
                })//
				.filter(Objects::nonNull)//
				.toList()//
		;
		LOG.trace("EXIT: createAccounts found {} accounts", created.size());
		return created;
	}

	@Override
	public String createToken(String loginNr, Set<String> roles) {
		LOG.trace("ENTER: createToken; loginNr={{}}, roles={{}}", loginNr, roles);
		String token = Jwt.issuer("https://your-app.example")
				.upn(loginNr)//
				.groups(roles)//
				.expiresIn(Duration.ofHours(2))//
				.sign()//
		;
		LOG.trace("EXIT: createToken; token={{}}", token);
		return token;
	}

	@Override
	public DatabaseResponse getDatabaseResponse() {
		List<AccountResponseFull> accounts = accountDao.listAll().stream()//
				.map(account -> {
					return new AccountResponseFull(
							responseMapper.getAccountResponse(account),
							responseMapper.getCreditResponseFull(account.getCredit()),
							responseMapper.getCartResponseSimple(account.getCart()),
							account.getFoodOrders().stream()//
									.map(responseMapper::getFoodOrderResponseFull)//
									.toList(),
							responseMapper.getFoodCourtResponseFull(account.getFoodCourt())
					);
				})
				.toList()//
		;
		return new DatabaseResponse(
			accounts
		);
	}

	/*
		Private Helper Functions
	 */

	private AccountResponse createAccount(String loginNr, String rawPassword, UUID id) throws ServiceException {
		LOG.trace("ENTER: createAccount; loginNr={{}}, rawPassword={{}}, id={{}}", loginNr, rawPassword, id);
		if (accountDao.existsByLoginNr(loginNr)) {
			LOG.error("loginNr={{}} already exists", loginNr);
			throw new ServiceException("loginNr {" + loginNr + "} already exists.", Response.Status.BAD_REQUEST);
		}
		Ticket ticket;
		try {
			ticket = accountDao.getTicketByLoginNr(loginNr) ;
		} catch (DaoException e) {
			LOG.error("could not get ticket for loginNr={{}} Exception:", loginNr, e);
			throw new ServiceException(e, Response.Status.NOT_FOUND);
		}

		AccountType type = getAccountTypeFromLoginNr(loginNr);
		String hashedPassword = BCrypt.withDefaults().hashToString(12, rawPassword.trim().toCharArray());
		Account account = new Account(id, ticket, hashedPassword, type);

		if(type == AccountType.GUEST) {
			LOG.info("account type is GUEST, creating cart and credit");
			Credit credit = new Credit(INITIAL_CREDIT, account);
			account.setCredit(credit);
			Cart cart = new Cart(false,  0, account);
			account.setCart(cart);
		}
		accountDao.persist(account);
		AccountResponse response = responseMapper.getAccountResponse(account);
		LOG.trace("EXIT: createAccount; account={{}}", response);
		return response;
	}

	private AccountResponse createAccount(AccountRequest request) throws ServiceException {
		LOG.trace("ENTER: createAccount; request={{}}", request);
		String loginNr = request.loginNr();
		String rawPassword = request.password();
		UUID id = request.id();
		AccountResponse response = createAccount(loginNr, rawPassword, id);
		LOG.trace("EXIT: createAccount; account={{}}", response);
		return response;
	}

	private AccountType getAccountTypeFromLoginNr(String loginNr) throws ServiceException {
		LOG.trace("ENTER: getAccountTypeFromLoginNr; loginNr={{}}", loginNr);
		if(loginNr.startsWith("V")) {
			LOG.trace("accountType=GUEST");
			return AccountType.GUEST;
		} else if (loginNr.startsWith("F")) {
			LOG.trace("accountType=FOOD_COURT_WORKER");
			return AccountType.FOOD_COURT_WORKER;
		} else if (loginNr.startsWith("A")) {
			LOG.trace("accountType=ADMIN");
			return AccountType.ADMIN;
		} else {
			LOG.error("accountType=UNKNOWN; invalid loginNr format {{}}", loginNr);
			throw new ServiceException("Invalid loginNr format. Must start with 'V', 'F', or 'A'.", Response.Status.BAD_REQUEST);
		}
	}

}
