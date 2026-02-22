package com.ffb.app.repository.api.product;

import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface ProductRepository extends PanacheRepositoryBase<Product, UUID> {

    List<Product> listByLoginNr(String loginNr);

    List<Product> listByFoodCourtId(UUID foodCourtId);

    void persistLink(MainSubProductLink link) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;
}
