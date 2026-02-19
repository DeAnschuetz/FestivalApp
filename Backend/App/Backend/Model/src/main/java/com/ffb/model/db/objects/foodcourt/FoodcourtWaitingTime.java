package com.ffb.model.db.objects.foodcourt;

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
@Table(name = "foodcourt_waiting_time", schema = "ffb")
public class FoodcourtWaitingTime extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
	@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodcourt_id", referencedColumnName = "id")
	private Foodcourt foodcourt;

    @Column(name = "waiting_time")
	private int waitingTime;

    protected FoodcourtWaitingTime() {}
    
	public FoodcourtWaitingTime(int waitingTime) {
		super();
		this.waitingTime = waitingTime;
	}
}
