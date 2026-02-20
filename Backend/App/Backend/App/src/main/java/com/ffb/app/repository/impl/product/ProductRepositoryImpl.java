package com.ffb.app.repository.impl.product;

import com.ffb.app.repository.api.product.ProductRepository;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

    public List<Product> listByFoodCourtId(UUID foodCourtId) {
        return list("foodCourt.id", foodCourtId);
    }

    public void persistLink(MainSubProductLink link) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {
        getEntityManager().persist(link);
    }

    public MainSubProductLink findLinkById(UUID linkId) throws IllegalArgumentException {
        return getEntityManager().find(MainSubProductLink.class, linkId);
    }

    public boolean linkExists(UUID mainId, UUID subId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException {
        Long cnt = getEntityManager()
                .createQuery("""
                        select count(l)
                        from MainSubProductLink l
                        where l.mainProduct.id = :mainId and l.subProduct.id = :subId
                        """, Long.class)
                .setParameter("mainId", mainId)
                .setParameter("subId", subId)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    public List<MainSubProductLink> listLinks() throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return getEntityManager()
                .createQuery("""
                        select l
                        from MainSubProductLink l
                        """, MainSubProductLink.class)
                .getResultList()//
                ;
    }

    public List<MainSubProductLink> listLinksByMain(UUID mainId) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return getEntityManager()
                .createQuery("""
                        select l
                        from MainSubProductLink l
                        where l.mainProduct.id = :mainId
                        """, MainSubProductLink.class)
                .setParameter("mainId", mainId)
                .getResultList()//
        ;
    }

    public long deleteLinkByPair(UUID mainId, UUID subId) throws IllegalArgumentException, IllegalStateException, PersistenceException  {
        return getEntityManager()
                .createQuery("""
                        delete from MainSubProductLink l
                        where l.mainProduct.id = :mainId and l.subProduct.id = :subId
                        """)
                .setParameter("mainId", mainId)
                .setParameter("subId", subId)
                .executeUpdate();
    }

    public boolean deleteLinkById(UUID linkId) throws IllegalArgumentException, TransactionRequiredException {
        MainSubProductLink link = findLinkById(linkId);
        if (link == null) {
            return false;
        }
        getEntityManager().remove(link);
        return true;
    }
}
