package com.ffb.model.objects.notification;

import java.util.UUID;

public class Notification {

	private UUID id;
	private UUID accountID;
	private NotificationStatus status;
	private String message;

	public Notification(UUID id, UUID accountID, NotificationStatus status, String message) {
		super();
		this.id = id;
		this.accountID = accountID;
		this.status = status;
		this.message = message;
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
