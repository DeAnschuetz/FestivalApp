package com.ffb.app.dao.api.product;

import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface ProductDao {

    List<Product> listAll();

    List<Product> listByLoginNr(String loginNr);

    List<Product> listByFoodCourtId(UUID foodCourtId);

    void persistLink(MainSubProductLink link) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException;

    boolean linkExists(UUID mainId, UUID subId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException;

    List<MainSubProductLink> listLinks() throws IllegalArgumentException, IllegalStateException, PersistenceException;

    List<MainSubProductLink> listLinksByMain(UUID mainId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    long deleteLinkByPair(UUID mainId, UUID subId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    boolean deleteLinkById(UUID linkId) throws IllegalArgumentException, TransactionRequiredException;

    void persist(Product product);

    Product getById(UUID id);

    void delete(Product product);
}
