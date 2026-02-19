package com.ffb.model.db.objects.account;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.credit.Credit;
import com.ffb.model.db.objects.foodorder.FoodOrder;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account", schema = "ffb")
public class Account extends PanacheEntityBase {

	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "login_nr", unique = true, nullable = false)
	private String loginNr;

	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType type;

	@JsonIgnore
	@OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Cart cart;

	@JsonIgnore
	@OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Credit credit;

	@JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrder> foodOrder;

	protected Account() {
	}

	public Account(UUID id, String loginNr, String password, AccountType type) {
		super();
		this.id = id;
		this.loginNr = loginNr;
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
		return loginNr;
	}

	public void setLoginNr(String loginNr) {
		this.loginNr = loginNr;
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

	public List<FoodOrder> getFoodOrder() {
		return foodOrder;
	}

	public void setFoodOrder(List<FoodOrder> foodOrder) {
		this.foodOrder = foodOrder;
	}

}
