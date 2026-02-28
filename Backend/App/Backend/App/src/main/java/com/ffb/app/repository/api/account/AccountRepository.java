package com.ffb.app.repository.api.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ffb.model.db.object.account.Account;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public interface AccountRepository extends PanacheRepositoryBase<Account, UUID> {

	Optional<Account> getByLoginNr(String loginNr);
	
	boolean existsByLoginNr(String loginNr);
	
	List<Account> getAllAccounts();

}
