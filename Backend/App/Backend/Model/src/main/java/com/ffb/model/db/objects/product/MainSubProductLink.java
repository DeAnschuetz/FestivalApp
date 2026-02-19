package com.ffb.model.db.objects.product;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "main_sub_product", schema = "ffb")
public class MainSubProductLink {

	  @GeneratedValue
	  @Id
	  public UUID id;

	  @ManyToOne(fetch = FetchType.LAZY, optional = false)
	  @JoinColumn(name = "main_product_id", referencedColumnName = "id")
	  public Product main;

	  @ManyToOne(fetch = FetchType.LAZY, optional = false)
	  @JoinColumn(name = "sub_product_id", referencedColumnName = "id")
	  public Product sub;

	  protected MainSubProductLink() {}
}
