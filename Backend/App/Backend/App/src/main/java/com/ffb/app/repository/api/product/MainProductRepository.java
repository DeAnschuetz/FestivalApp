package com.ffb.app.repository.api.product;

import com.ffb.model.db.objects.product.MainProduct;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;
import java.util.UUID;

public interface MainProductRepository extends PanacheRepositoryBase<MainProduct, UUID> {

    Optional<MainProduct> getMainProductByProductId(UUID id);
}
