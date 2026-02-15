package com.ffb.model.objects.foodcourt;

import java.util.UUID;

public class FoodcourtWaitingTime {

	private UUID foodcourtID;
	private int waitingTime;

	public FoodcourtWaitingTime(UUID foodcourtID, int waitingTime) {
		super();
		this.foodcourtID = foodcourtID;
		this.waitingTime = waitingTime;
	}

	public UUID getFoodcourtID() {
		return foodcourtID;
	}

	public void setFoodcourtID(UUID foodcourtID) {
		this.foodcourtID = foodcourtID;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	@Override
	public String toString() {
		return "FoodcourtWaitingTime [foodcourtID=" + foodcourtID + ", waitingTime=" + waitingTime + "]";
	}

}
