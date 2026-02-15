package com.ffb.model.objects.foodorder;

import java.time.LocalDateTime;
import java.util.UUID;

public class FoodOrderHistory {

	private UUID id;
	private UUID foodOrderID;
	private LocalDateTime statusChangeTime;
	private FoodOrderStatus oldStatus;
	private FoodOrderStatus newStatus;

	public FoodOrderHistory(UUID id, UUID foodOrderID, LocalDateTime statusChangeTime, FoodOrderStatus oldStatus,
			FoodOrderStatus newStatus) {
		super();
		this.id = id;
		this.foodOrderID = foodOrderID;
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

	public UUID getFoodOrderID() {
		return foodOrderID;
	}

	public void setFoodOrderID(UUID foodOrderID) {
		this.foodOrderID = foodOrderID;
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

	@Override
	public String toString() {
		return "FoodOrderHistory [id=" + id + ", foodOrderID=" + foodOrderID + ", statusChangeTime=" + statusChangeTime
				+ ", oldStatus=" + oldStatus + ", newStatus=" + newStatus + "]";
	}

}
