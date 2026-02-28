package com.ffb.model.db.object.credit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.account.Account;

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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "credit", schema = "ffb")
public class Credit extends PanacheEntityBase {
        
    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

	@JdbcTypeCode(SqlTypes.DECIMAL)
	@Column(name = "amount")
	private double amount;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;

	@JsonIgnore
    @OneToMany(mappedBy = "credit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CreditHistory> creditHistory;
    
    protected Credit() {}

	public Credit(double amount, Account account) {
		super();
		this.id = UUID.randomUUID();
		this.amount = amount;
		this.account = account;
		this.creditHistory = new ArrayList<>();
		this.creditHistory.add(
				new CreditHistory(
						UUID.randomUUID(),
						0,
						amount,
						LocalDateTime.now(),
						this
				)
		);
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		if (this.creditHistory == null) {
			this.creditHistory = new ArrayList<>();
		}
		this.creditHistory.add(new CreditHistory(
			UUID.randomUUID(),
			this.getAmount(),
			amount,
			LocalDateTime.now(),
			this
		));
		this.amount = amount;
	}

	public List<CreditHistory> getCreditHistory() {
		return creditHistory;
	}

	public void setCreditHistory(List<CreditHistory> creditHistory) {
		this.creditHistory = creditHistory;
	}
	
	
}
