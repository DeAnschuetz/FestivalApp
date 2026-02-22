package com.ffb.model.db.objects.product;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import com.ffb.model.db.objects.foodorder.FoodOrderItem;

@Entity
@Immutable
@Table(name = "v_main_product", schema = "ffb")
public class MainProduct extends ProductPrototype {
    
    @OneToMany(mappedBy = "mainProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubProduct> subProducts;

	protected MainProduct() {}

	public MainProduct(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
		super(id, price, displayName, symbolIdentifier, minimalWarning);
	}
}
