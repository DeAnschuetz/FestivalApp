package com.ffb.model.objects.notification;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderNotification extends Notification {

	private NotificationType type;
	private LocalDateTime orderTime;
	private LocalDateTime pickupTime;

	public OrderNotification(UUID id, UUID accountID, NotificationStatus status, String message, NotificationType type,
			LocalDateTime orderTime, LocalDateTime pickupTime) {
		super(id, accountID, status, message);
		this.type = type;
		this.orderTime = orderTime;
		this.pickupTime = pickupTime;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
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

	@Override
	public String toString() {
		return "OrderNotification [type=" + type + ", orderTime=" + orderTime + ", pickupTime=" + pickupTime + "]";
	}

}
