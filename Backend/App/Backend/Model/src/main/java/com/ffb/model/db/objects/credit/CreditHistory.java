package com.ffb.model.objects.credit;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreditHistory {

	private UUID id;
	private UUID accountID;
	private double oldAmmount;
	private double newAmmount;
	private LocalDateTime changeTime;

	public CreditHistory(UUID id, UUID accountID, double oldAmmount, double newAmmount, LocalDateTime changeTime) {
		super();
		this.id = id;
		this.accountID = accountID;
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

	public UUID getAccountID() {
		return accountID;
	}

	public void setAccountID(UUID accountID) {
		this.accountID = accountID;
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

	@Override
	public String toString() {
		return "creditHistory [id=" + id + ", accountID=" + accountID + ", oldAmmount=" + oldAmmount + ", newAmmount="
				+ newAmmount + ", changeTime=" + changeTime + "]";
	}

}
