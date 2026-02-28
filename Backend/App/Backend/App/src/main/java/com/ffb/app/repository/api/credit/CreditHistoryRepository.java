package com.ffb.app.repository.api.credit;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.object.credit.CreditHistory;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.PersistenceException;

public interface CreditHistoryRepository extends PanacheRepositoryBase<CreditHistory, UUID> {

	List<CreditHistory> findHistoryByLoginNr(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException;
}
