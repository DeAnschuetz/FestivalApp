package com.ffb.model.objects.foodcourt;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "foodcourt_waiting_time", schema = "ffb")
public class FoodcourtWaitingTime extends PanacheEntityBase {

    @Id
    @Column(name = "foodcourt_id")
	private UUID foodcourtID;

    @Column(name = "waiting_time")
	private int waitingTime;

    protected FoodcourtWaitingTime() {}
    
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
