package com.ffb.app.service.impl.credit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.ffb.app.repository.api.credit.CreditRepository;
import com.ffb.app.service.api.credit.CreditService;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.credit.Credit;

import com.ffb.model.db.objects.credit.CreditHistory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    private final int INITIAL_AMMOUNT = 1000;

    private final CreditRepository creditRepo;

    public CreditServiceImpl(CreditRepository creditRepo) {
        this.creditRepo = creditRepo;
    }

    @Transactional
    public Credit createInitialCredit(Account account) {
        if (account == null) throw new IllegalArgumentException("account must not be null");

        UUID accountId = account.getId();
        if (creditRepo.existsByAccountId(accountId)) {
            throw new IllegalStateException("Credit already exists for accountId=" + accountId);
        }

        Credit credit = new Credit(UUID.randomUUID(), INITIAL_AMMOUNT, account);
        creditRepo.persist(credit);

        // initial history row (0 -> initialAmount)
        CreditHistory h = new CreditHistory(
                UUID.randomUUID(),
                0.0,
                INITIAL_AMMOUNT,
                LocalDateTime.now()
        );
        h.setAccount(account);
        h.setCredit(credit);
        creditRepo.persistHistory(h);

        return credit;
    }

    public Credit getByAccountId(UUID accountId) {
        return creditRepo.findByAccountId(accountId)//
                .orElseThrow(() -> new IllegalArgumentException("Credit not found for accountId=" + accountId));
    }

    public Credit findByLoginNr(String loginNr) {
        return creditRepo.findByLoginNr(loginNr)//
                .orElseThrow(() -> new IllegalArgumentException("Credit not found for loginNr=" + loginNr));
    }

    @Transactional
    public Credit addAmount(UUID accountId, double delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta must be > 0");

        Credit credit = getByAccountId(accountId);
        double oldAmount = credit.getAmmount();
        double newAmount = oldAmount + delta;

        credit.setAmmount(newAmount);

        CreditHistory h = new CreditHistory(
                UUID.randomUUID(),
                oldAmount,
                newAmount,
                LocalDateTime.now()
        );
        h.setAccount(credit.getAccount());
        h.setCredit(credit);
        creditRepo.persistHistory(h);

        return credit;
    }

    @Transactional
    public Credit subtractAmount(UUID accountId, double delta) {
        if (delta <= 0) throw new IllegalArgumentException("delta must be > 0");

        Credit credit = getByAccountId(accountId);
        double oldAmount = credit.getAmmount();
        double newAmount = oldAmount - delta;

        if (newAmount < 0) {
            throw new IllegalStateException("Insufficient credit");
        }

        credit.setAmmount(newAmount);

        CreditHistory h = new CreditHistory(
                UUID.randomUUID(),
                oldAmount,
                newAmount,
                LocalDateTime.now()
        );
        h.setAccount(credit.getAccount());
        h.setCredit(credit);
        creditRepo.persistHistory(h);

        return credit;
    }

    public List<CreditHistory> getHistoryForAccount(UUID accountId, int pageIndex, int pageSize) {
        return creditRepo.findHistoryByAccountId(accountId, pageIndex, pageSize);
    }

    public List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize) {
        return creditRepo.findHistoryByCreditId(creditId, pageIndex, pageSize);
    }
}
