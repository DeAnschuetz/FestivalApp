package com.ffb.model.db.object.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.account.Account;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "cart", schema = "ffb")
public class Cart extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;

	@JdbcTypeCode(SqlTypes.BOOLEAN)
    @Column(name = "has_prio")
	private boolean hasPrio;

	@JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "total")
	private double total;
	
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	private List<CartItem> cartItems = new ArrayList<>();;
    
    protected Cart() {}

	public Cart(UUID id, boolean hasPrio, double total, Account account) {
		super();
		this.id = id;
		this.hasPrio = hasPrio;
		this.total = total;
		this.account = account;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
}
