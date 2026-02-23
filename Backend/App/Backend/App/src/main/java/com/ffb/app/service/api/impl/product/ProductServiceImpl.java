package com.ffb.app.service.api.impl.product;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.service.api.api.product.ProductService;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    private final FoodCourtDao foodCourtDao;

    @Inject
    public ProductServiceImpl(ProductDao productDao, FoodCourtDao foodCourtDao) {
        this.productDao = productDao;
        this.foodCourtDao = foodCourtDao;
    }

    @Override
    public List<Product> listProducts() {
        return productDao.listAll();
    }

    @Override
    public List<Product> listProductsByLoginNr(String loginNr) {
        return productDao.listByLoginNr(loginNr);
    }

    @Override
    @Transactional
    public Product createProductByLoginNr(String loginNr, double price, String displayName, String symbolIdentifier, int minimalWarning) throws ServiceException {
        UUID id = UUID.randomUUID();
        FoodCourt foodcourt;
        try {
            foodcourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Product product = new Product(id, price, displayName, symbolIdentifier, minimalWarning, foodcourt);
        productDao.persist(product);
        return product;
    }

    @Override
    public List<Product> listProductsByFoodCourtId(UUID foodCourtId) {
        return productDao.listByFoodCourtId(foodCourtId);
    }

    @Override
    public Product getProductById(UUID id) throws NotFoundException {
        return productDao.getById(id);
    }

    @Override
    @Transactional
    public Product createProductWithId(UUID id, UUID foodCourtId, double price, String displayName, String symbolIdentifier, int minimalWarning) throws ServiceException {
        if (productDao.getById(id) != null) {
            throw new ServiceException("Product already exists: " + id, Response.Status.NOT_FOUND);
        }

        FoodCourt foodcourt;
        try {
            foodcourt = foodCourtDao.getById(foodCourtId);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Product product = new Product(id, price, displayName, symbolIdentifier, minimalWarning, foodcourt);
        productDao.persist(product);
        return product;
    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) throws ServiceException {
        Product p = productDao.getById(id);
        if (p == null) {
            throw new ServiceException("Product not found: " + id, Response.Status.NOT_FOUND);
        }
        productDao.delete(p);
    }

    @Override
    @Transactional
    public boolean createAssignment(UUID mainProductId, UUID subProductId) throws ServiceException {

        Product main = productDao.getById(mainProductId);
        if (main == null) {
            throw new ServiceException("Main product not found: " + mainProductId, Response.Status.NOT_FOUND);
        }

        Product sub = productDao.getById(subProductId);
        if (sub == null) {
            throw new ServiceException("Sub product not found: " + subProductId, Response.Status.NOT_FOUND);
        }

        if (productDao.linkExists(mainProductId, subProductId)) {
            throw new ServiceException("Assignment already exists", Response.Status.NOT_FOUND);
        }

        MainSubProductLink link = new MainSubProductLink(main, sub);

        productDao.persistLink(link);
        return true;
    }

    @Override
    public List<MainSubProductLink> listAssignmentsForMainProduct(UUID mainProductId) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return productDao.listLinksByMain(mainProductId);
    }

    @Override
    @Transactional
    public void deleteAssignmentById(UUID linkId) throws NotFoundException, IllegalArgumentException, TransactionRequiredException {
        boolean ok = productDao.deleteLinkById(linkId);
        if (!ok) throw new NotFoundException("Assignment not found: " + linkId);
    }

    @Override
    @Transactional
    public void deleteAssignmentByPair(UUID mainProductId, UUID subProductId) throws ServiceException {
        long deleted = productDao.deleteLinkByPair(mainProductId, subProductId);
        if (deleted == 0) {
            throw new ServiceException("Assignment not found for given pair", Response.Status.NOT_FOUND);
        }
    }
}
