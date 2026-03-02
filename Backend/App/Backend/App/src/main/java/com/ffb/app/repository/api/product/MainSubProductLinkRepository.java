package com.ffb.app.repository.api.product;

import com.ffb.model.db.object.product.MainSubProductLink;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface MainSubProductLinkRepository extends PanacheRepositoryBase <MainSubProductLink, UUID> {

    boolean linkExists(UUID mainId, UUID subId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException;

    List<MainSubProductLink> listLinks() throws IllegalArgumentException, IllegalStateException, PersistenceException;

    List<MainSubProductLink> listLinksByMain(UUID mainId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    long deleteLinkByPair(UUID mainId, UUID subId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    boolean deleteLinkById(UUID linkId) throws IllegalArgumentException, TransactionRequiredException;
}
