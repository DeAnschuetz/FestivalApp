package com.ffb.model.db.view;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@PersistenceUnit(unitName = "virtual")
@Table(name = "v_main_product", schema = "virtual")
public class MainProduct extends ProductPrototype {
    
    @OneToMany(mappedBy = "mainProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubProduct> subProducts;

	protected MainProduct() {}

	public MainProduct(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
		super(id, price, displayName, symbolIdentifier, minimalWarning);
	}
}
