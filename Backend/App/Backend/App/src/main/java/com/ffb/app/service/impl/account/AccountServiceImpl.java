package com.ffb.app.service.impl.account;



import com.ffb.app.repository.api.account.AccountRepository;
import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.app.service.api.account.AccountService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.db.objects.credit.Credit;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepo;
	private final CreditRepository creditRepo;

	@Inject
	public AccountServiceImpl(AccountRepository accountRepo, CreditRepository creditRepo) {
		this.accountRepo = accountRepo;
		this.creditRepo = creditRepo;
	}

	@Transactional
	public Account createAccount(String loginNr, String rawPassword) {
		if (loginNr == null || loginNr.isBlank()) {
			throw new IllegalArgumentException("loginNr must not be blank");
		}
		if (rawPassword == null || rawPassword.isBlank()) {
			throw new IllegalArgumentException("password must not be blank");
		}

		if (accountRepo.existsByLoginNr(loginNr)) {
			throw new IllegalStateException("loginNr already exists");
		}

		AccountType type = null;
		try {
			type = getAccountTypeFromLoginNr(loginNr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UUID id = UUID.randomUUID();
		String hashedPassword = BcryptUtil.bcryptHash(rawPassword);

		Account account = new Account(id, loginNr, hashedPassword, type);
		Credit credit = new Credit(UUID.randomUUID(), 1000, account);

		accountRepo.persist(account);
		creditRepo.persist(credit);
		return account;
	}
	
	/**
	 * Verifies credentials (loginNr + rawPassword). Returns true if the account
	 * exists and the password matches.
	 */
	public boolean verifyAccount(String loginNr, String rawPassword) {
		if (loginNr == null || rawPassword == null) {
			return false;
		}

		return accountRepo.findByLoginNr(loginNr)//
				.map(acc -> BcryptUtil.matches(rawPassword, acc.getPassword()))
				.orElse(false)//
		;
	}
	
	public List<Account> getAllAccounts() {
		List<Account> result = accountRepo.getAllAccounts();
		return result;
	}
	
	private AccountType getAccountTypeFromLoginNr(String loginNr) throws Exception {
		if(loginNr.startsWith("V")) {
			return AccountType.FESTIVAL_GUEST;
		} else if (loginNr.startsWith("F")) {
			return AccountType.FOOCOURT_WORKER;
		} else if (loginNr.startsWith("A")) {
			return AccountType.FESTIVAL_ADMIN;
		} else {
			throw new Exception();
		}
	}
	
}
