package com.ffb.model.db.objects.notification;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.foodorder.FoodOrderStatus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @Column(name = "order_time")
	private LocalDateTime orderTime;

	@JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
    @Column(name = "pickup_time")
	private LocalDateTime pickupTime;

    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    
    protected FoodOrderNotification() {}

	public FoodOrderNotification(UUID id, NotificationStatus status, String message) {
		super();
		this.id = id;
		this.status = status;
		this.message = message;
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

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
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
}
