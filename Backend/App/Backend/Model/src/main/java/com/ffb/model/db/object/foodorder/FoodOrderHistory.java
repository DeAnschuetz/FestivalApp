package com.ffb.model.db.object.foodorder;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "food_order_history", schema = "ffb")
public class FoodOrderHistory extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

	@JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
    @Column(name = "status_change_time")
	private LocalDateTime statusChangeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
	private FoodOrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
	private FoodOrderStatus newStatus;

    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_order_id", referencedColumnName = "id")
    private FoodOrder order;

    protected FoodOrderHistory() {} 
    
	public FoodOrderHistory(LocalDateTime statusChangeTime, FoodOrderStatus oldStatus, FoodOrderStatus newStatus, FoodOrder order) {
		super();
		this.id = UUID.randomUUID();
		this.order = order;
		this.statusChangeTime = statusChangeTime;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getStatusChangeTime() {
		return statusChangeTime;
	}

	public void setStatusChangeTime(LocalDateTime statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	public FoodOrderStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(FoodOrderStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public FoodOrderStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(FoodOrderStatus newStatus) {
		this.newStatus = newStatus;
	}

	public FoodOrder getOrder() {
		return order;
	}

	public void setOrder(FoodOrder foodOrder) {
		this.order = foodOrder;
	}
}
