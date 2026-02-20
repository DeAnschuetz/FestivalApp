package com.ffb.app.service.impl.account;



import com.ffb.app.dao.api.account.AccountDao;
import com.ffb.app.dao.api.credit.CreditDao;

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

	private final AccountDao accountDao;
	private final CreditDao creditDao;

	@Inject
	public AccountServiceImpl(AccountDao accountDao, CreditDao creditDao) {
		this.accountDao = accountDao;
		this.creditDao = creditDao;
	}

	@Override
	@Transactional
	public Account createAccount(String loginNr, String rawPassword) {
		if (loginNr == null || loginNr.isBlank()) {
			throw new IllegalArgumentException("loginNr must not be blank");
		}
		if (rawPassword == null || rawPassword.isBlank()) {
			throw new IllegalArgumentException("password must not be blank");
		}

		if (accountDao.existsByLoginNr(loginNr)) {
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

		accountDao.persist(account);
		creditDao.persist(credit);
		return account;
	}
	
	/**
	 * Verifies credentials (loginNr + rawPassword). Returns true if the account
	 * exists and the password matches.
	 */
	@Override
	public boolean verifyAccount(String loginNr, String rawPassword) {
		if (loginNr == null || rawPassword == null) {
			return false;
		}

		return accountDao.findByLoginNr(loginNr)//
				.map(acc -> BcryptUtil.matches(rawPassword, acc.getPassword()))
				.orElse(false)//
		;
	}

	@Override
	public List<Account> getAllAccounts() {
		List<Account> result = accountDao.getAll();
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
