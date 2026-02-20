package com.ffb.app.repository.api.credit;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.objects.credit.CreditHistory;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public interface CreditHistoryRepository extends PanacheRepositoryBase<CreditHistory, UUID> {
	
	List<CreditHistory> findByAccountId(UUID accountId, int pageIndex, int pageSize);
	
	List<CreditHistory> findByCreditId(UUID creditId, int pageIndex, int pageSize);

}
