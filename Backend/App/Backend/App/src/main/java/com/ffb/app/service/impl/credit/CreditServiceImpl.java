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
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreditServiceImpl implements CreditService {

    private final int INITIAL_AMMOUNT = 1000;

    private final CreditRepository creditRepo;

    @Inject
    public CreditServiceImpl(CreditRepository creditRepo) {
        this.creditRepo = creditRepo;
    }

    @Transactional
    public Credit createInitialCredit(Account account) throws IllegalStateException {
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
        creditRepo.persistHistory(h);

        return credit;
    }

    public Credit getByLoginNr(String loginNr) throws IllegalArgumentException {
        return creditRepo.findByLoginNr(loginNr)//
                .orElseThrow(() -> new IllegalArgumentException("Credit not found for loginNr=" + loginNr));
    }

    @Transactional
    public Credit addAmount(String loginNr, double delta) throws IllegalArgumentException {
        if (delta <= 0) {
            throw new IllegalArgumentException("delta must be > 0");
        }

        Credit credit = getByLoginNr(loginNr);
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
    public Credit subtractAmount(String loginNr, double delta) throws IllegalStateException {
        if (delta <= 0) throw new IllegalArgumentException("delta must be > 0");

        Credit credit = getByLoginNr(loginNr);
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

    public List<CreditHistory> getHistoryForAccount(String loginNr, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditRepo.findHistoryByAccountId(loginNr, pageIndex, pageSize);
    }

    public List<CreditHistory> getHistoryForCredit(UUID creditId, int pageIndex, int pageSize) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return creditRepo.findHistoryByCreditId(creditId, pageIndex, pageSize);
    }
}
