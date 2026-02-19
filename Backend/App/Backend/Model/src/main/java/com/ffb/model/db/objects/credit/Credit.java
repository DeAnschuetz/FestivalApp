package com.ffb.model.db.objects.credit;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.account.Account;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit", schema = "ffb")
public class Credit extends PanacheEntityBase {
        
    @Id
    @Column(name = "id")
	private UUID id;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;

	@Column(name = "ammount")
	private double ammount;
    
    @JsonIgnore
    @OneToMany(mappedBy = "credit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CreditHistory> creditHistory;
    
    protected Credit() {}

	public Credit(UUID id, double ammount, Account account) {
		super();
		this.id = id;
		this.ammount = ammount;
		this.account = account;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public double getAmmount() {
		return ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}

	public List<CreditHistory> getCreditHistory() {
		return creditHistory;
	}

	public void setCreditHistory(List<CreditHistory> creditHistory) {
		this.creditHistory = creditHistory;
	}
	
	
}
