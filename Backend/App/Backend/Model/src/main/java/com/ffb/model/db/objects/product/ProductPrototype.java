package com.ffb.model.db.objects.product;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.food_court.FoodCourt;

import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@MappedSuperclass
public class ProductPrototype extends PanacheEntityBase {

    @Id
	@JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

	@JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "price", precision = 10, scale = 2)
	private double price;

	@JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "display_name", length =  100, nullable = false)
	private String displayName;

	@JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "symbol_identifier", length =  200, nullable = false)
	private String symbolIdentifier;

	@JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "minimal_warning")
	private int minimalWarning;
    
    @JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_court_id", referencedColumnName = "id")
    private FoodCourt foodCourt;

    protected ProductPrototype() {}

	public ProductPrototype(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning, FoodCourt foodCourt) {
		this.id = id;
		this.price = price;
		this.displayName = displayName;
		this.symbolIdentifier = symbolIdentifier;
		this.minimalWarning = minimalWarning;
		this.foodCourt = foodCourt;
	}

	public ProductPrototype(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
		super();
		this.id = id;
		this.price = price;
		this.displayName = displayName;
		this.symbolIdentifier = symbolIdentifier;
		this.minimalWarning = minimalWarning;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSymbolIdentifier() {
		return symbolIdentifier;
	}

	public void setSymbolIdentifier(String symbolIdentifier) {
		this.symbolIdentifier = symbolIdentifier;
	}

	public int getMinimalWarning() {
		return minimalWarning;
	}

	public void setMinimalWarning(int minimalWarning) {
		this.minimalWarning = minimalWarning;
	}

	public FoodCourt getFoodCourt() {
		return foodCourt;
	}

	public void setFoodCourt(FoodCourt foodCourt) {
		this.foodCourt = foodCourt;
	}

}
