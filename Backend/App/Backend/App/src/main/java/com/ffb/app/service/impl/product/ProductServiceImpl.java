package com.ffb.app.service.impl.product;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.api.request.product.ProductLinkRequest;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.product.ProductRequestSimple;
import com.ffb.model.api.response.product.ProductResponse;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.db.object.product.MainSubProductLink;
import com.ffb.model.db.object.product.Product;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    // TODO Logging

    private final ProductDao productDao;

    private final FoodCourtDao foodCourtDao;

    @Inject
    public ProductServiceImpl(ProductDao productDao, FoodCourtDao foodCourtDao) {
        this.productDao = productDao;
        this.foodCourtDao = foodCourtDao;
    }

    @Override
    @Transactional
    public List<ProductResponse> listProducts() {
        return productDao.listAll()
                .stream()//
                .map(this::getProductResponse)//
                .toList()//
        ;
    }

    @Override
    public List<ProductResponse> listProductsByLoginNr(String loginNr) {
        return productDao.listByLoginNr(loginNr)
                .stream()//
                .map(this::getProductResponse)//
                .toList()//
        ;
    }

    @Override
    @Transactional
    public ProductResponse createProductByLoginNr(String loginNr, ProductRequestSimple request) throws ServiceException {
        UUID id = UUID.randomUUID();
        double price = request.price();
        String displayName = request.displayName();
        String symbolIdentifier = request.symbolIdentifier();
        int minimalWarning = request.minimalWarning();
        FoodCourt foodcourt;
        try {
            foodcourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Product product = new Product(id, price, displayName, symbolIdentifier, minimalWarning, foodcourt);
        productDao.persist(product);
        return getProductResponse(product);
    }

    @Override
    public List<ProductResponse> listProductsByFoodCourtId(UUID foodCourtId) {
        return productDao.listByFoodCourtId(foodCourtId)//
                .stream()//
                .map(this::getProductResponse)//
                .toList()//
        ;
    }

    @Override
    public ProductResponse getProductById(UUID id) throws NotFoundException {
        Product product = productDao.getById(id);
        return getProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProductWithId(UUID foodCourtId, ProductRequest request) throws ServiceException {
        UUID id = request.id();
        double price = request.price();
        String displayName = request.displayName();
        String symbolIdentifier = request.symbolIdentifier();
        int minimalWarning = request.minimalWarning();

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
        return getProductResponse(product);
    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) throws ServiceException {
        Product product = productDao.getById(id);
        if (product == null) {
            throw new ServiceException("Product not found: " + id, Response.Status.NOT_FOUND);
        }
        productDao.delete(product);
    }

    @Override
    @Transactional
    public boolean createAssignment(ProductLinkRequest request) throws ServiceException {
        UUID mainProductId = request.mainProductId();
        UUID subProductId = request.subProductId();
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
    @Transactional
    public void deleteAssignmentById(UUID linkId) throws ServiceException {
        boolean ok = productDao.deleteLinkById(linkId);
        if (!ok) throw new ServiceException("Assignment not found: " + linkId, Response.Status.NOT_FOUND);
    }

    @Transactional
    @Override
    public void deleteAssignmentByPair(ProductLinkRequest request) throws ServiceException {
        UUID mainProductId = request.mainProductId();
        if(mainProductId == null){
            throw new ServiceException("Main product not found", Response.Status.BAD_REQUEST);
        }
        UUID subProductId = request.subProductId();
        if(subProductId == null){
            throw new ServiceException("Sub product not found", Response.Status.BAD_REQUEST);
        }
        long deleted = productDao.deleteLinkByPair(mainProductId, subProductId);
        if (deleted == 0) {
            throw new ServiceException("Assignment not found for given pair", Response.Status.NOT_FOUND);
        }
    }

    @Override
    public void createProducts(UUID id, List<ProductRequest> productRequests) {
        productRequests.stream()
                .map(request -> {
                    try {
                        return createProductWithId(id, request);
                    } catch (ServiceException e) {
//                        LOG.error("could not create")
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList()
        ;

    }

    @Override
    public void createLinks(List<ProductLinkRequest> productLinkRequests) {
        productLinkRequests.stream()
                .map(request -> {
                    try {
                        return createAssignment(request);
                    } catch (ServiceException e) {
//                        LOG.error("");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList()
        ;
    }

    /*
        Private Helper Functions
    */
    private ProductResponse getProductResponse(Product product) {
        List<ProductResponse> subProducts = product.getSubProducts().stream().map(this::getProductResponse).toList();
        return new ProductResponse(
                product.getId(),
                product.getPrice(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                product.getMinimalWarning(),
                product.getCount(),
                subProducts
        );
    }
}
