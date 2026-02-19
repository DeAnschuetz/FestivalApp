package com.ffb.app.repository.api.account;

import java.util.List;
import java.util.Optional;

import com.ffb.model.db.objects.account.Account;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface AccountRepository extends PanacheRepository<Account> {

	public Optional<Account> findByLoginNr(String loginNr);
	
	public boolean existsByLoginNr(String loginNr);
	
	public List<Account> getAllAccounts();
}
