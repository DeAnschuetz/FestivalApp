package com.ffb.app.repository.api.credit;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.objects.credit.CreditHistory;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

public interface CreditHistoryRepository extends PanacheRepositoryBase<CreditHistory, UUID> {

	void persistHistory(CreditHistory history) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;

	List<CreditHistory> findHistoryByAccountId(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;

	List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;
}
