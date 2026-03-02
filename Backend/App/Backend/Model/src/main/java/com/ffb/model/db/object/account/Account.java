package com.ffb.model.db.object.account;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.db.object.credit.Credit;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.db.object.foodorder.FoodOrder;

import com.ffb.model.db.object.notification.FoodOrderNotification;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "account", schema = "ffb")
public class Account extends PanacheEntityBase {

	@Id
	@JdbcTypeCode(SqlTypes.UUID)
	@Column(name = "id", unique = true, nullable = false)
	private UUID id;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "login_nr", referencedColumnName = "login_nr", unique = true, nullable = false)
	private Ticket ticket;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "password", length = 60, nullable = false)
	private String password;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType type;

	@JsonIgnore
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Cart cart;

	@JsonIgnore
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Credit credit;

	@JsonIgnore
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private FoodCourt foodCourt;

	@JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrder> foodOrders;

	@JsonIgnore
	@OneToMany(mappedBy = "sharedAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrder> sharedOrders;

	@JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderNotification> notifications;

	protected Account() {
	}

	public Account(UUID id, Ticket ticket, String password, AccountType type) {
		this.id = id;
		this.ticket = ticket;
		this.password = password;
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLoginNr() {
		return ticket.getLoginNr();
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public FoodCourt getFoodCourt() {
		return foodCourt;
	}

	public void setFoodCourt(FoodCourt foodCourt) {
		this.foodCourt = foodCourt;
	}

	public List<FoodOrder> getFoodOrders() {
		return foodOrders;
	}

	public void setFoodOrders(List<FoodOrder> foodOrders) {
		this.foodOrders = foodOrders;
	}

	public List<FoodOrder> getSharedOrders() {
		return sharedOrders;
	}

	public void setSharedOrders(List<FoodOrder> sharedOrders) {
		this.sharedOrders = sharedOrders;
	}

	public List<FoodOrderNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<FoodOrderNotification> notifications) {
		this.notifications = notifications;
	}
}
