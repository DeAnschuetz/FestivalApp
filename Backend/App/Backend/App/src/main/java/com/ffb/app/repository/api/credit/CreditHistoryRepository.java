package com.ffb.app.repository.api.credit;

import java.util.List;
import java.util.UUID;

import com.ffb.model.db.objects.credit.CreditHistory;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CreditHistoryRepository extends PanacheRepository<CreditHistory> {
	
	public List<CreditHistory> findByAccountId(UUID accountId, int pageIndex, int pageSize);
	
	 public List<CreditHistory> findByCreditId(UUID creditId, int pageIndex, int pageSize);

}
