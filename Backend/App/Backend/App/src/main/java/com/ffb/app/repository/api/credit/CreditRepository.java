package com.ffb.app.repository.api.credit;

import java.util.Optional;
import java.util.UUID;

import com.ffb.model.db.object.credit.Credit;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public interface CreditRepository extends PanacheRepositoryBase<Credit, UUID> {

	Optional<Credit> findByLoginNr(String loginNr);

	boolean existsByLoginNr(String loginNr);
}
