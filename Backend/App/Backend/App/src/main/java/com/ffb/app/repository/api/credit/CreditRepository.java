package com.ffb.app.repository.api.credit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.PersistenceException;

public interface CreditRepository extends PanacheRepositoryBase<Credit, UUID> {

	Optional<Credit> findByLoginNr(String loginNr);

	boolean existsByLoginNr(String loginNr);
}
