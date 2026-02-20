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

@Entity
@Table(name = "food_order_notification", schema = "ffb")
public class FoodOrderNotification extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FoodOrderStatus type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
	private NotificationStatus status;

    @Column(name = "message")
	private String message;
    
    @Column(name = "order_time")
	private LocalDateTime orderTime;
	
    @Column(name = "pickup_time")
	private LocalDateTime pickupTime;
    
    @JsonIgnore
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

	@Override
	public String toString() {
		return "Notification [message=" + message + "]";
	}

}
