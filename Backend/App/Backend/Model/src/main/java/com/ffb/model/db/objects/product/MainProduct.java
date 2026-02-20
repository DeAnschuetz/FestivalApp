package com.ffb.model.db.objects.product;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Immutable;

import com.ffb.model.db.objects.foodorder.FoodOrderItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "v_main_product", schema = "ffb")
public class MainProduct extends ProductPrototype {
    
    @OneToMany(mappedBy = "mainProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubProduct> subProducts;
    
    @OneToMany(mappedBy = "mainProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodOrderItem> foodOrderItems;
    
	protected MainProduct() {}

	public MainProduct(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
		super(id, price, displayName, symbolIdentifier, minimalWarning);
	}
}
