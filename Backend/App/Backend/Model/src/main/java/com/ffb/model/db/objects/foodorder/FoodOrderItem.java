package com.ffb.model.objects.foodorder;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "food_order_item", schema = "ffb")
public class FoodOrderItem extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
	
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_order_id", referencedColumnName = "id")
	private FoodOrder foodOrder;
    
    @Column(name = "product_id")
	private UUID productID;
	
    @Column(name = "price")
    private double price;
	
    @Column(name = "item_count")
    private int itemCount;
	
    @Column(name = "extra")
    private String extra;
    
    protected FoodOrderItem() {}

	public FoodOrderItem(UUID id, FoodOrder foodOrder, UUID productID, double price, int itemCount, String extra) {
		super();
		this.id = id;
		this.foodOrder = foodOrder;
		this.productID = productID;
		this.price = price;
		this.itemCount = itemCount;
		this.extra = extra;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

    @JsonIgnore
	public FoodOrder getFoodOrder() {
		return foodOrder;
	}

    @JsonIgnore
	public void setFoodOrder(FoodOrder foodOrder) {
		this.foodOrder = foodOrder;
	}

	public UUID getProductID() {
		return productID;
	}

	public void setProductID(UUID productID) {
		this.productID = productID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return "FoodOrderItems [id=" + id + ", productID=" + productID + ", price=" + price
				+ ", itemCount=" + itemCount + ", extra=" + extra + "]";
	}

}
