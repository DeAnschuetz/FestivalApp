package com.ffb.app.service.api.account;

import java.util.List;

import com.ffb.model.db.objects.account.Account;

public interface AccountService {
	
	public Account createAccount(String loginNr, String rawPassword);
	
	public boolean verifyAccount(String loginNr, String rawPassword);
	
	public List<Account> getAllAccounts();

}
