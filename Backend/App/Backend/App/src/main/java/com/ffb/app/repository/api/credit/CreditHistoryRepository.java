package com.ffb.app.repository.api.credit;

import com.ffb.model.db.object.credit.CreditHistory;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.UUID;

public interface CreditHistoryRepository extends PanacheRepositoryBase<CreditHistory, UUID> {

	List<CreditHistory> findHistoryByLoginNr(String loginNr, int pageIndex, int pageSize);
}
