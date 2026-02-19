package com.ffb.model.objects.credit;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit", schema = "ffb")
public class Credit extends PanacheEntityBase {

    @Id
    @Column(name = "account_id")
	private UUID accountId;
    
    @Id
    @Column(name = "ammount")
	private double ammount;
    
    protected Credit() {}

	public Credit(UUID accountId, double ammount) {
		super();
		this.accountId = accountId;
		this.ammount = ammount;
	}

	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

	public double getAmmount() {
		return ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}

	@Override
	public String toString() {
		return "Credit [accountId=" + accountId + ", ammount=" + ammount + "]";
	}

}
