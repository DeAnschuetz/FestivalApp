package com.ffb.app.service.api.account;

import java.util.List;

import com.ffb.model.db.objects.account.Account;

public interface AccountService {
	
	Account createAccount(String loginNr, String rawPassword);
	
	boolean verifyAccount(String loginNr, String rawPassword);
	
	List<Account> getAllAccounts();

}
