package com.ffb.app.service.api.api.account;

import java.util.List;

import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.account.AccountType;
import com.ffb.model.exception.ServiceException;

public interface AccountService {

	Account getAccountByLoginNr(String loginNr) throws ServiceException;

	Account createAccount(String loginNr, String rawPassword) throws ServiceException;

	AccountType verifyAccount(String loginNr, String rawPassword) throws ServiceException;
	
	List<Account> getAllAccounts();

}
