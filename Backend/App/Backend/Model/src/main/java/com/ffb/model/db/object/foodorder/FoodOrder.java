package com.ffb.model.db.object.foodorder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.food_court.FoodCourt;

import com.ffb.model.db.object.notification.FoodOrderNotification;
import com.ffb.model.db.object.notification.NotificationStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
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

	@JdbcTypeCode(SqlTypes.LOCAL_DATE_TIME)
	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderItem> items;
    
    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderHistory> history;

	@JsonIgnore
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderNotification> notifications;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
			name = "account_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_account"),
			unique = false,
			nullable = false
	)
    private Account account;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "shared_account_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_shared_account"),
			unique = false,
			nullable = true
	)
	private Account sharedAccount;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
			name = "food_court_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_food_court"),
			unique = false,
			nullable = false
	)
    private FoodCourt foodCourt;

    protected FoodOrder() {}

	public FoodOrder(boolean hasPrio, double total, List<FoodOrderItem> items, FoodCourt foodCourt, Account account) {
		this.id = UUID.randomUUID();
		this.status = FoodOrderStatus.ORDERED;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = foodCourt.getWaitingTime();
		this.isHidden = false;
		this.items = items;
		this.orderTime = LocalDateTime.now();
		this.account = account;
		this.foodCourt = foodCourt;
		this.notifications = new ArrayList<>();
		this.notifications.add(
				new FoodOrderNotification(
					status,
					NotificationStatus.NEW,
					"Order {" + id + "} was placed",
					orderTime,
					orderTime.plusMinutes(foodCourt.getWaitingTime()),
					this,
					account
				)
		);
		this.history = new ArrayList<>();
		this.history.add(
				new FoodOrderHistory(
					LocalDateTime.now(),
					null,
					FoodOrderStatus.ORDERED,
					this
				)
		);
	}

	public FoodOrder(UUID id, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, LocalDateTime orderTime, List<FoodOrderItem> items, FoodCourt foodCourt) {
		this.id = id;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = false;
		this.items = items;
		this.foodCourt = foodCourt;
	}

	public FoodOrder(UUID id, FoodOrderStatus status, boolean hasPrio, double total, int waitingTime, LocalDateTime orderTime, List<FoodOrderItem> items) {
		super();
		this.id = id;
		this.status = status;
		this.hasPrio = hasPrio;
		this.total = total;
		this.waitingTime = waitingTime;
		this.isHidden = false;
		this.items = items;
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
		this.history.add(
				new FoodOrderHistory(
						LocalDateTime.now(),
						this.getStatus(),
						status,
						this
				)
		);

		LocalDateTime estimatedPickupTime = orderTime.plusMinutes(foodCourt.getWaitingTime());
		if(status == FoodOrderStatus.READY_FOR_PICKUP) {
			estimatedPickupTime = LocalDateTime.now();
		}
		this.notifications.add(
				new FoodOrderNotification(
						status,
						NotificationStatus.NEW,
						"Order {" + id + "} was updated to " + status.toString(),
						LocalDateTime.now(),
						estimatedPickupTime,
						this,
						account
				)
		);
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

	public List<FoodOrderItem> getItems() {
		return items;
	}

	public void setItems(List<FoodOrderItem> foodOrderItems) {
		this.items = foodOrderItems;
	}

	public List<FoodOrderHistory> getHistory() {
		return history;
	}

	public void setHistory(List<FoodOrderHistory> foodOrderHistory) {
		this.history = foodOrderHistory;
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

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	public List<FoodOrderNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<FoodOrderNotification> notifications) {
		this.notifications = notifications;
	}
}
