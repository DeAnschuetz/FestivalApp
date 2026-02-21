package com.ffb.model.db.objects.product;

import java.util.UUID;

import com.ffb.model.db.objects.id.MainSubProductId;
import jakarta.persistence.*;

@Entity
@Table(name = "main_sub_product", schema = "ffb")
public class MainSubProductLink {

	@EmbeddedId
	public MainSubProductId id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("mainProductId")
	@JoinColumn(name = "main_product_id", referencedColumnName = "id")
	public Product mainProduct;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("subProductId")
	@JoinColumn(name = "sub_product_id", referencedColumnName = "id")
	public Product subProduct;

	protected MainSubProductLink() {}


	public MainSubProductLink(Product mainProduct, Product subProduct) {
		this.mainProduct = mainProduct;
		this.subProduct = subProduct;
		this.id = new MainSubProductId(mainProduct.getId(), subProduct.getId());
	}

	public MainSubProductId getId() {
		return id;
	}

	public void setId(MainSubProductId id) {
		this.id = id;
	}

	public Product getMainProduct() {
		return mainProduct;
	}

	public void setMainProduct(Product mainProduct) {
		this.mainProduct = mainProduct;
	}

	public Product getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(Product subProduct) {
		this.subProduct = subProduct;
	}
}
