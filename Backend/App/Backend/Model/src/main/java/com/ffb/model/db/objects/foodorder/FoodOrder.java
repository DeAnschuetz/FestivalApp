package com.ffb.model.objects.foodorder;

import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_order", schema = "ffb")
public class FoodOrder extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
    @Column(name = "account_id")
	private UUID accountID;
    
    @Column(name = "foodcourt_id")
	private UUID foodcourtID;
    
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
    
    protected FoodOrder() {}

	public FoodOrder(UUID id, UUID accountID, UUID foodcourtID, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, boolean isHidden, List<FoodOrderItem> foodOrderItems) {
		super();
		this.id = id;
		this.accountID = accountID;
		this.foodcourtID = foodcourtID;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = isHidden;
		this.foodOrderItems = foodOrderItems;
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
		return foodOrderItems;
	}

	public void setItems(List<FoodOrderItem> items) {
		this.foodOrderItems = items;
	}
	
	public static List<FoodOrder> listAllWithItems() {
	    return find("SELECT DISTINCT c FROM FoodOrder c LEFT JOIN FETCH c.foodOrderItems").list();
	}

	@Override
	public String toString() {
		return "FoodOrder [id=" + id + ", accountID=" + accountID + ", foodcourtID=" + foodcourtID + ", status="
				+ status + ", hasPrio=" + hasPrio + ", total=" + total + ", waitingTime=" + waitingTime + ", isHidden="
				+ isHidden + ", items=" + foodOrderItems + "]";
	}

}
