package com.ffb.app.repository.api.account;

import java.util.List;
import java.util.Optional;

import com.ffb.model.db.objects.account.Account;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface AccountRepository extends PanacheRepository<Account> {

	Optional<Account> getByLoginNr(String loginNr);
	
	boolean existsByLoginNr(String loginNr);
	
	List<Account> getAllAccounts();

}
