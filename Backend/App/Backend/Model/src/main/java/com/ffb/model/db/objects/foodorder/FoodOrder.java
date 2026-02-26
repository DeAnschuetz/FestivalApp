package com.ffb.model.db.objects.foodorder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.food_court.FoodCourt;

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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "food_order", schema = "ffb")
public class FoodOrder extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;
      
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
	private FoodOrderStatus status;

	@JdbcTypeCode(SqlTypes.BOOLEAN)
    @Column(name = "has_prio")
	private boolean hasPrio;

	@JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "total")
	private double total;

	@JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "waiting_time")
	private int waitingTime;

	@JdbcTypeCode(SqlTypes.BOOLEAN)
    @Column(name = "is_hidden")
	private boolean isHidden;
    
    @OneToMany(mappedBy = "foodOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderItem> foodOrderItems;
    
    @JsonIgnore
    @OneToMany(mappedBy = "foodOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderHistory> foodOrderHistory;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shared_account_id", referencedColumnName = "id")
	private Account sharedAccount;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_court_id", referencedColumnName = "id")
    private FoodCourt foodCourt;

    protected FoodOrder() {}

	public FoodOrder(UUID id, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, List<FoodOrderItem> foodOrderItems, FoodCourt foodCourt, Account account) {
		this.id = id;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = false;
		this.foodOrderItems = foodOrderItems;
		this.account = account;
		this.foodCourt = foodCourt;
	}

	public FoodOrder(UUID id, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, List<FoodOrderItem> foodOrderItems, FoodCourt foodCourt) {
		this.id = id;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = false;
		this.foodOrderItems = foodOrderItems;
		this.foodCourt = foodCourt;
	}

	public FoodOrder(UUID id, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, List<FoodOrderItem> foodOrderItems) {
		super();
		this.id = id;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = false;
		this.foodOrderItems = foodOrderItems;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public void setHidden(boolean hidden) {
		isHidden = hidden;
	}

	public List<FoodOrderItem> getFoodOrderItems() {
		return foodOrderItems;
	}

	public void setFoodOrderItems(List<FoodOrderItem> foodOrderItems) {
		this.foodOrderItems = foodOrderItems;
	}

	public List<FoodOrderHistory> getFoodOrderHistory() {
		return foodOrderHistory;
	}

	public void setFoodOrderHistory(List<FoodOrderHistory> foodOrderHistory) {
		this.foodOrderHistory = foodOrderHistory;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getSharedAccount() {
		return sharedAccount;
	}

	public void setSharedAccount(Account sharedAccount) {
		this.sharedAccount = sharedAccount;
	}

	public FoodCourt getFoodCourt() {
		return foodCourt;
	}

	public void setFoodCourt(FoodCourt foodCourt) {
		this.foodCourt = foodCourt;
	}
}
