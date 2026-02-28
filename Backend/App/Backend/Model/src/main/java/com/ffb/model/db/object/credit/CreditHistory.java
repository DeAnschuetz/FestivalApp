package com.ffb.model.db.object.credit;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "credit_history", schema = "ffb")
public class CreditHistory extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

	@JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "old_amount")
	private double oldAmount;

	@JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "new_amount")
	private double newAmount;

	@JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
    @Column(name = "change_time")
	private LocalDateTime changeTime;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    private Credit credit;

    
    protected CreditHistory() {}

	public CreditHistory(UUID id, double oldAmount, double newAmount, LocalDateTime changeTime, Credit credit) {
		this.id = id;
		this.oldAmount = oldAmount;
		this.newAmount = newAmount;
		this.changeTime = changeTime;
		this.credit = credit;
	}

	public CreditHistory(UUID id, double oldAmount, double newAmount, LocalDateTime changeTime) {
		super();
		this.id = id;
		this.oldAmount = oldAmount;
		this.newAmount = newAmount;
		this.changeTime = changeTime;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(double oldAmount) {
		this.oldAmount = oldAmount;
	}

	public double getNewAmount() {
		return newAmount;
	}

	public void setNewAmount(double newAmount) {
		this.newAmount = newAmount;
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
}
