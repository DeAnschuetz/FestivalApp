package com.ffb.model.db.objects.foodorder;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_order_history", schema = "ffb")
public class FoodOrderHistory extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
    @Column(name = "status_change_time")
	private LocalDateTime statusChangeTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
	private FoodOrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
	private FoodOrderStatus newStatus;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_order_id", referencedColumnName = "id")
    private FoodOrder foodOrder;

    protected FoodOrderHistory() {} 
    
	public FoodOrderHistory(UUID id, FoodOrder foodOrder, LocalDateTime statusChangeTime, FoodOrderStatus oldStatus,
			FoodOrderStatus newStatus) {
		super();
		this.id = id;
		this.foodOrder = foodOrder;
		this.statusChangeTime = statusChangeTime;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
	}

}
