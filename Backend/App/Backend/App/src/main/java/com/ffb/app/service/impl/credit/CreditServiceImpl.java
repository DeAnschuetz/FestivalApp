package com.ffb.app.service.impl.credit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ffb.app.repository.api.credit.CreditHistoryRepository;
import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.app.service.api.credit.CreditService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    private final int INITIAL_AMMOUNT = 1000;

    private final CreditRepository creditRepo;
    private final CreditHistoryRepository creditHistoryRepo;

    @Inject
    public CreditServiceImpl(CreditRepository creditRepo, CreditHistoryRepository creditHistoryRepo) {
        this.creditRepo = creditRepo;
        this.creditHistoryRepo = creditHistoryRepo;
    }

    @Transactional
    public void createInitialCredit(Account account) throws IllegalStateException {
        if (account == null) throw new IllegalArgumentException("account must not be null");

        String loginNr = account.getLoginNr();
        if (creditRepo.existsByLoginNr(loginNr)) {
            throw new IllegalStateException("Credit already exists for loginNr=" + loginNr);
        }

        Credit credit = new Credit(UUID.randomUUID(), INITIAL_AMMOUNT, account);
        creditRepo.persist(credit);

        CreditHistory h = new CreditHistory(
                UUID.randomUUID(),
                0.0,
                INITIAL_AMMOUNT,
                LocalDateTime.now()
        );
        h.setAccount(account);
        h.setCredit(credit);
        creditHistoryRepo.persistHistory(h);

    }

    public Credit getByLoginNr(String loginNr) throws IllegalArgumentException {
        return creditRepo.findByLoginNr(loginNr)//
                .orElseThrow(() -> new IllegalArgumentException("Credit not found for loginNr=" + loginNr));
    }

    @Transactional
    public Credit changeAmount(String loginNr, double amount) throws IllegalStateException {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }

        Credit credit = getByLoginNr(loginNr);
        double oldAmount = credit.getAmount();
        double newAmount = oldAmount - amount;

        if (newAmount < 0) {
            throw new IllegalStateException("Insufficient credit");
        }
        credit.setAmount(newAmount);

        CreditHistory creditHistory = new CreditHistory(
                UUID.randomUUID(),
                oldAmount,
                newAmount,
                LocalDateTime.now()
        );
        creditHistory.setAccount(credit.getAccount());
        creditHistory.setCredit(credit);

        creditRepo.persist(credit);
        creditHistoryRepo.persistHistory(creditHistory);
        return credit;
    }

    public List<CreditHistory> getHistoryForAccount(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditHistoryRepo.findHistoryByAccountId(loginNr, pageIndex, pageSize);
    }

    public List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditHistoryRepo.findHistoryByCreditId(creditId, pageIndex, pageSize);
    }
}
