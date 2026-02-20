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

    List<Product> listByFoodcourtId(UUID foodcourtId);

    void persistLink(MainSubProductLink link) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;

    MainSubProductLink findLinkById(UUID linkId) throws IllegalArgumentException;

    boolean linkExists(UUID mainId, UUID subId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException;

    List<MainSubProductLink> listLinks() throws IllegalArgumentException, IllegalStateException, PersistenceException;

    List<MainSubProductLink> listLinksByMain(UUID mainId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    long deleteLinkByPair(UUID mainId, UUID subId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    boolean deleteLinkById(UUID linkId) throws IllegalArgumentException, TransactionRequiredException;
}
