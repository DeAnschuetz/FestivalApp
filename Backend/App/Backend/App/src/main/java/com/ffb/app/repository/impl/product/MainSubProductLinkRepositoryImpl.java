package com.ffb.app.repository.impl.product;

import com.ffb.app.repository.api.product.MainSubProductLinkRepository;
import com.ffb.model.db.objects.product.MainSubProductLink;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class MainSubProductLinkRepositoryImpl implements MainSubProductLinkRepository {


    @Override
    public MainSubProductLink findLinkById(UUID linkId) throws IllegalArgumentException {
        return getEntityManager().find(MainSubProductLink.class, linkId);
    }

    @Override
    public boolean linkExists(UUID mainId, UUID subId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException {
        long cnt = count("mainProduct.id = ?1 and subProduct.id = ?2", mainId, subId);
        return cnt > 0;
    }

    @Override
    public List<MainSubProductLink> listLinks() throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return listAll();
    }

    @Override
    public List<MainSubProductLink> listLinksByMain(UUID mainId) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return list("mainProduct.id", mainId);
    }

    @Override
    public long deleteLinkByPair(UUID mainId, UUID subId) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return delete(
                    "mainProduct.id = :mainId and subProduct.id = :subId",
                Parameters.with("mainId", mainId).and("subId", subId)
                )//
        ;
    }

    @Override
    public boolean deleteLinkById(UUID linkId) throws IllegalArgumentException, TransactionRequiredException {
        return deleteById(linkId);
    }

}
