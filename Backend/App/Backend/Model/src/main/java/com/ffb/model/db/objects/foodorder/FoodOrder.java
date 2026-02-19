package com.ffb.model.db.objects.foodorder;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.foodcourt.Foodcourt;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_order", schema = "ffb")
public class FoodOrder extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
      
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
	private FoodOrderStatus status;
    
    @Column(name = "has_prio")
	private boolean hasPrio;
    
    @Column(name = "total")
	private double total;
    
    @Column(name = "waiting_time")
	private int waitingTime;
    
    @Column(name = "is_hidden")
	private boolean isHidden;
    
    @OneToMany(mappedBy = "foodOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderItem> foodOrderItems;
    
    @JsonIgnore
    @OneToMany(mappedBy = "foodOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderHistory> foodOrderHistory;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodcourt_id", referencedColumnName = "id")
    private Foodcourt foodcourt;

    protected FoodOrder() {}

	public FoodOrder(UUID id, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, boolean isHidden, List<FoodOrderItem> foodOrderItems) {
		super();
		this.id = id;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = isHidden;
		this.foodOrderItems = foodOrderItems;
	}
	
	public static List<FoodOrder> listAllWithItems() {
	    return find("SELECT DISTINCT c FROM FoodOrder c LEFT JOIN FETCH c.foodOrderItems").list();
	}

}
