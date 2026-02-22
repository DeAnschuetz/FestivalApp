package com.ffb.model.db.objects.food_court;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.account.Account;
import com.ffb.model.db.objects.foodorder.FoodOrder;

import com.ffb.model.db.objects.image.Image;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "food_court", schema = "ffb")
public class FoodCourt extends PanacheEntityBase {

    @Id
	@GeneratedValue
    @Column(name = "id")
	private UUID id;

	@Column(name = "display_name")
	private String displayName;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", referencedColumnName = "id")
	private Account account;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "image")
	byte[] image;
    
    @OneToOne(mappedBy = "foodCourt", cascade = CascadeType.ALL)
    private FoodCourtWaitingTime waitingTime;
    
    @JsonIgnore
    @OneToMany(mappedBy = "foodCourt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodOrder> foodOrder;
    
    protected FoodCourt() {}
    
	public FoodCourt(String displayName) {
		super();
		this.displayName = displayName;
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

	public FoodCourtWaitingTime getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(FoodCourtWaitingTime waitingTime) {
		this.waitingTime = waitingTime;
	}

	public List<FoodOrder> getFoodOrder() {
		return foodOrder;
	}

	public void setFoodOrder(List<FoodOrder> foodOrder) {
		this.foodOrder = foodOrder;
	}
}
