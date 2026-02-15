package com.ffb.model.objects.foodorder;

import java.util.List;
import java.util.UUID;

public class FoodOrder {

	private UUID id;
	private UUID accountID;
	private UUID foodcourtID;
	private FoodOrderStatus status;
	private boolean hasPrio;
	private double total;
	private int waitingTime;
	private boolean isHidden;
	private List<FoodOrderItem> items;

	public FoodOrder(UUID id, UUID accountID, UUID foodcourtID, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, boolean isHidden, List<FoodOrderItem> items) {
		super();
		this.id = id;
		this.accountID = accountID;
		this.foodcourtID = foodcourtID;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = isHidden;
		this.items = items;
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

	public UUID getFoodcourtID() {
		return foodcourtID;
	}

	public void setFoodcourtID(UUID foodcourtID) {
		this.foodcourtID = foodcourtID;
	}

	public FoodOrderStatus getStatus() {
		return status;
	}

	public void setStatus(FoodOrderStatus status) {
		this.status = status;
	}

	public boolean isHasPrio() {
		return hasPrio;
	}

	public void setHasPrio(boolean hasPrio) {
		this.hasPrio = hasPrio;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public List<FoodOrderItem> getItems() {
		return items;
	}

	public void setItems(List<FoodOrderItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "FoodOrder [id=" + id + ", accountID=" + accountID + ", foodcourtID=" + foodcourtID + ", status="
				+ status + ", hasPrio=" + hasPrio + ", total=" + total + ", waitingTime=" + waitingTime + ", isHidden="
				+ isHidden + ", items=" + items + "]";
	}

}
