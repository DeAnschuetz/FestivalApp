package com.ffb.app.repository.api.credit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CreditRepository extends PanacheRepository<Credit> {
	
	public Optional<Credit> findByAccountId(UUID accountId);

	public Optional<Credit> findByLoginNr(String loginNr);

	public boolean existsByAccountId(UUID accountId);

	public void persistHistory(CreditHistory history);

	public List<CreditHistory> findHistoryByAccountId(UUID accountId, int pageIndex, int pageSize);

	public List<CreditHistory> findHistoryByCreditId(UUID creditId, int pageIndex, int pageSize);

}
