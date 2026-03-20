package com.ffb.model.db.object.notification;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderStatus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "food_order_notification", schema = "ffb")
public class FoodOrderNotification extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FoodOrderStatus type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
	private NotificationStatus status;

	@JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "message", length = 255, nullable = true)
	private String message;

	@JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
    @Column(name = "creation_time")
	private LocalDateTime creationTime;

	@JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
    @Column(name = "pickup_time")
	private LocalDateTime pickupTime;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "order_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_food_order"),
			unique = false,
			nullable = false
	)
	private FoodOrder order;

    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
			name = "account_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_account"),
			unique = false,
			nullable = false
	)
    private Account account;
    
    protected FoodOrderNotification() {}

	public FoodOrderNotification(FoodOrderStatus type, NotificationStatus status, String message, LocalDateTime creationTime, LocalDateTime pickupTime, FoodOrder order, Account account) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.status = status;
		this.message = message;
		this.creationTime = creationTime;
		this.pickupTime = pickupTime;
		this.account = account;
		this.order = order;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public FoodOrderStatus getType() {
		return type;
	}

	public void setType(FoodOrderStatus type) {
		this.type = type;
	}

	public NotificationStatus getStatus() {
		return status;
	}

	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime orderTime) {
		this.creationTime = orderTime;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public FoodOrder getOrder() {
		return order;
	}

	public void setOrder(FoodOrder order) {
		this.order = order;
	}
}
