package com.ffb.model.db.objects.food_court;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;



@Entity
@Table(name = "food_court_waiting_time", schema = "ffb")
public class FoodCourtWaitingTime extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
	@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_court_id", referencedColumnName = "id")
	private FoodCourt foodCourt;

    @Column(name = "waiting_time")
	private int waitingTime;

    protected FoodCourtWaitingTime() {}
    
	public FoodCourtWaitingTime(int waitingTime) {
		super();
		this.waitingTime = waitingTime;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public FoodCourt getFoodCourt() {
		return foodCourt;
	}

	public void setFoodCourt(FoodCourt foodCourt) {

		this.foodCourt = foodCourt;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
}
