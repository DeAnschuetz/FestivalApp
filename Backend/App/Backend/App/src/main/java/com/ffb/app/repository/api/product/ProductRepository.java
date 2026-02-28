package com.ffb.app.repository.api.product;

import com.ffb.model.db.object.product.MainSubProductLink;
import com.ffb.model.db.object.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends PanacheRepositoryBase<Product, UUID> {

    List<Product> listByLoginNr(String loginNr);

    List<Product> listByFoodCourtId(UUID foodCourtId);

    void persistLink(MainSubProductLink link) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;
}
