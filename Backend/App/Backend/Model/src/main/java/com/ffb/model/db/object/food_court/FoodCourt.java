package com.ffb.model.db.object.food_court;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.foodorder.FoodOrder;

import com.ffb.model.db.object.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "food_court", schema = "ffb")
public class FoodCourt extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "display_name", length =  100, nullable = false)
	private String displayName;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "image")
	byte[] image;

	@OneToOne(mappedBy = "foodCourt", cascade = CascadeType.ALL)
	private FoodCourtWaitingTime waitingTime;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "account_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_account"),
			unique = true,
			nullable = false
	)
	private Account account;

	@JsonIgnore
	@OneToMany(mappedBy = "foodCourt", fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Product> products;

	@JsonIgnore
    @OneToMany(mappedBy = "foodCourt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodOrder> foodOrders;

    protected FoodCourt() {}
    
	public FoodCourt(String displayName, Account account) {
		super();
		this.id = UUID.randomUUID();
		this.displayName = displayName;
		this.account = account;
		this.waitingTime = new FoodCourtWaitingTime(this, 0);
	}

	public FoodCourt(UUID id, String displayName, Account account) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.account = account;
		this.waitingTime = new FoodCourtWaitingTime(this, 0);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public FoodCourtWaitingTime getWaitingTimeObject() {
		return waitingTime;
	}

	public int getWaitingTime() {
		return waitingTime.getWaitingTime();
	}

	public void setWaitingTime(FoodCourtWaitingTime waitingTime) {
		this.waitingTime = waitingTime;
	}

	public void updateWaitingTime(int newWaitingTime) {
		this.waitingTime.setWaitingTime(newWaitingTime);
	}

	public List<FoodOrder> getFoodOrders() {
		return foodOrders;
	}

	public void setFoodOrders(List<FoodOrder> foodOrder) {
		this.foodOrders = foodOrder;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
