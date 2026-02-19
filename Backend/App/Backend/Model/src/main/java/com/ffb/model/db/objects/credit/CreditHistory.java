package com.ffb.model.db.objects.credit;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.account.Account;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_history", schema = "ffb")
public class CreditHistory extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;

    @Column(name = "old_ammount")
	private double oldAmmount;
    
    @Column(name = "new_ammount")
	private double newAmmount;
    
    @Column(name = "change_time")
	private LocalDateTime changeTime;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private Credit credit;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;
    
    protected CreditHistory() {}

	public CreditHistory(UUID id, double oldAmmount, double newAmmount, LocalDateTime changeTime) {
		super();
		this.id = id;
		this.oldAmmount = oldAmmount;
		this.newAmmount = newAmmount;
		this.changeTime = changeTime;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getOldAmmount() {
		return oldAmmount;
	}

	public void setOldAmmount(double oldAmmount) {
		this.oldAmmount = oldAmmount;
	}

	public double getNewAmmount() {
		return newAmmount;
	}

	public void setNewAmmount(double newAmmount) {
		this.newAmmount = newAmmount;
	}

	public LocalDateTime getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(LocalDateTime changeTime) {
		this.changeTime = changeTime;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}


}
