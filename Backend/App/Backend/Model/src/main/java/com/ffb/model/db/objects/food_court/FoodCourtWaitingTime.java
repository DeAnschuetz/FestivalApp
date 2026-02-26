package com.ffb.model.db.objects.food_court;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.cli.annotations.DefaultValue;
import jakarta.enterprise.inject.Default;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "food_court_waiting_time", schema = "ffb")
public class FoodCourtWaitingTime extends PanacheEntityBase {

	@Id
	@GeneratedValue
	@JdbcTypeCode(SqlTypes.UUID)
	@Column(name = "id")
	private UUID id;


	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_court_id", referencedColumnName = "id")
	private FoodCourt foodCourt;

	@JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "waiting_time", nullable = false, columnDefinition = "integer default 0 check (waiting_time >= 0)")
	private int waitingTime;

    protected FoodCourtWaitingTime() {}

	public FoodCourtWaitingTime(FoodCourt foodCourt, int waitingTime) {
		this.foodCourt = foodCourt;
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
