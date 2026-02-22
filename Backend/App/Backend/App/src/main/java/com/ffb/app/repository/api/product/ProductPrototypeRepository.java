package com.ffb.app.repository.api.product;

import com.ffb.model.db.objects.product.ProductPrototype;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;

public interface ProductPrototypeRepository extends PanacheRepositoryBase<ProductPrototype, UUID> {
}
