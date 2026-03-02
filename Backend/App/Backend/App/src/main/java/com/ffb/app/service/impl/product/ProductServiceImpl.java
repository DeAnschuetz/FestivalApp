package com.ffb.app.service.impl.product;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.mapper.api.ResponseMapper;
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

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    // TODO Logging fertig
    private final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final ProductDao productDao;
    private final FoodCourtDao foodCourtDao;
    private final ResponseMapper mapper;

    @Inject
    public ProductServiceImpl(ProductDao productDao, FoodCourtDao foodCourtDao, ResponseMapper mapper) {
        this.productDao = productDao;
        this.foodCourtDao = foodCourtDao;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public List<ProductResponse> listProducts() {
        LOG.trace("ENTER: listProducts");
        List<ProductResponse> products = productDao.listAll()
                .stream()//
                .map(mapper::getProductResponse)//
                .toList()//
        ;
        LOG.trace("EXIT: listProducts found {} products", products.size());
        return products;
    }

    @Override
    public List<ProductResponse> listProductsByLoginNr(String loginNr) {
        LOG.trace("ENTER: listProductsByLoginNr; loginNr={{}}", loginNr);
        List<ProductResponse> products = productDao.listByLoginNr(loginNr)
                .stream()//
                .map(mapper::getProductResponse)//
                .toList()//
        ;
        LOG.trace("EXIT: listProductsByLoginNr; loginNr={{}} found {} products", loginNr, products.size());
        return products;
    }

    @Override
    @Transactional
    public ProductResponse createProductByLoginNr(String loginNr, ProductRequestSimple request) throws ServiceException {
        LOG.trace("ENTER: createProductByLoginNr; loginNr={{}}, request=[{}]", loginNr, request);

        UUID id = UUID.randomUUID();
        double price = request.price();
        String displayName = request.displayName();
        String symbolIdentifier = request.symbolIdentifier();
        int minimalWarning = request.minimalWarning();
        FoodCourt foodcourt;
        try {
            foodcourt = foodCourtDao.getByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not find food court for loginNr={{}}; Exception:", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Product product = new Product(id, price, displayName, symbolIdentifier, minimalWarning, foodcourt);
        productDao.persist(product);

        ProductResponse response = mapper.getProductResponse(product);
        LOG.info("product created; id={{}}, loginNr={{}}", id, loginNr);
        LOG.trace("EXIT: createProductByLoginNr; response=[{}]", response);
        return response;
    }

    @Override
    public List<ProductResponse> listProductsByFoodCourtId(UUID foodCourtId) {
        LOG.trace("ENTER: listProductsByFoodCourtId; foodCourtId={{}}", foodCourtId);
        List<ProductResponse> products = productDao.listByFoodCourtId(foodCourtId)//
                .stream()//
                .map(mapper::getProductResponse)//
                .toList()//
        ;
        LOG.trace("EXIT: listProductsByFoodCourtId; foodCourtId={{}} found {} products", foodCourtId, products.size());
        return products;
    }

    @Override
    public ProductResponse getProductById(UUID id) throws NotFoundException {
        LOG.trace("ENTER: getProductById; id={{}}", id);
        Product product = productDao.getById(id);
        if (product == null) {
            LOG.error("product not found; id={{}}", id);
            throw new NotFoundException("Product not found: " + id);
        }
        ProductResponse response = mapper.getProductResponse(product);
        LOG.trace("EXIT: getProductById; id={{}} response=[{}]", id, response);
        return response;
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
            LOG.error("product already exists; id={{}}", id);
            throw new ServiceException("Product already exists: " + id, Response.Status.NOT_FOUND);
        }

        FoodCourt foodcourt;
        try {
            foodcourt = foodCourtDao.getById(foodCourtId);
        } catch (DaoException e) {
            LOG.error("could not find food court; foodCourtId={{}} Exception:", foodCourtId, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        Product product = new Product(id, price, displayName, symbolIdentifier, minimalWarning, foodcourt);
        productDao.persist(product);

        ProductResponse response = mapper.getProductResponse(product);
        LOG.info("product created; id={{}}, foodCourtId={{}}", id, foodCourtId);
        LOG.trace("EXIT: createProductWithId; response=[{}]", response);
        return response;
    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) throws ServiceException {
        LOG.trace("ENTER: deleteProductById; id={{}}", id);

        Product product = productDao.getById(id);
        if (product == null) {
            LOG.error("product not found; id={{}}", id);
            throw new ServiceException("Product not found: " + id, Response.Status.NOT_FOUND);
        }

        productDao.delete(product);
        LOG.info("product deleted; id={{}}", id);
        LOG.trace("EXIT: deleteProductById; id={{}}", id);
    }

    @Override
    @Transactional
    public boolean createAssignment(ProductLinkRequest request) throws ServiceException {
        LOG.trace("ENTER: createAssignment; request=[{}]", request);

        UUID mainProductId = request.mainProductId();
        UUID subProductId = request.subProductId();
        Product main = productDao.getById(mainProductId);

        if (main == null) {
            LOG.error("main product not found; mainProductId={{}}", mainProductId);
            throw new ServiceException("Main product not found: " + mainProductId, Response.Status.NOT_FOUND);
        }

        Product sub = productDao.getById(subProductId);
        if (sub == null) {
            LOG.error("sub product not found; subProductId={{}}", subProductId);
            throw new ServiceException("Sub product not found: " + subProductId, Response.Status.NOT_FOUND);
        }

        if (productDao.linkExists(mainProductId, subProductId)) {
            LOG.warn("assignment already exists; mainProductId={{}}, subProductId={{}}", mainProductId, subProductId);
            throw new ServiceException("Assignment already exists", Response.Status.NOT_FOUND);
        }

        MainSubProductLink link = new MainSubProductLink(main, sub);

        productDao.persistLink(link);

        LOG.info("assignment created; mainProductId={{}}, subProductId={{}}", mainProductId, subProductId);
        LOG.trace("EXIT: createAssignment; ok=true");
        return true;
    }

    @Override
    @Transactional
    public void deleteAssignmentById(UUID linkId) throws ServiceException {
        LOG.trace("ENTER: deleteAssignmentById; linkId={{}}", linkId);

        boolean ok = productDao.deleteLinkById(linkId);
        if (!ok) {
            LOG.error("assignment not found; linkId={{}}", linkId);
            throw new ServiceException("Assignment not found: " + linkId, Response.Status.NOT_FOUND);
        }

        LOG.info("assignment deleted; linkId={{}}", linkId);
        LOG.trace("EXIT: deleteAssignmentById; linkId={{}}", linkId);
    }

    @Transactional
    @Override
    public void deleteAssignmentByPair(@NonNull ProductLinkRequest request) throws ServiceException {
        LOG.trace("ENTER: deleteAssignmentByPair; request=[{}]", request);

        UUID mainProductId = request.mainProductId();
        UUID subProductId = request.subProductId();

        long deleted = productDao.deleteLinkByPair(mainProductId, subProductId);
        if (deleted == 0) {
            LOG.error("assignment not found for pair; mainProductId={{}}, subProductId={{}}", mainProductId, subProductId);
            throw new ServiceException("Assignment not found for given pair", Response.Status.NOT_FOUND);
        }
        LOG.info("assignment deleted for pair; mainProductId={{}}, subProductId={{}} deleted={}", mainProductId, subProductId, deleted);
        LOG.trace("EXIT: deleteAssignmentByPair; deleted={}", deleted);
    }

    @Override
    public void createProducts(UUID id, List<ProductRequest> productRequests) {
        LOG.trace("ENTER: createProducts; foodCourtId={{}}, productRequests=[{}]", id, productRequests);

        List<ProductResponse> created = productRequests.stream()//
                .map(request -> {
                        try {
                            return createProductWithId(id, request);
                        } catch (ServiceException e) {
                            LOG.error("could not create product; foodCourtId={{}}, request=[{}] Exception:", id, request, e);
                            return null;
                        }
                    }
                )//
                .filter(Objects::nonNull)//
                .toList()//
        ;

        LOG.trace("EXIT: createProducts; foodCourtId={{}} created={}", id, created.size());
    }

    @Override
    @Transactional
    public void createLinks(List<ProductLinkRequest> productLinkRequests) {
        LOG.trace("ENTER: createLinks; productLinkRequests=[{}]", productLinkRequests);

        List<Boolean> created = productLinkRequests.stream()//
                .map(request -> {
                        try {
                            return createAssignment(request);
                        } catch (ServiceException e) {
                            LOG.error("could not create link; request=[{}] Exception:", request, e);
                            return null;
                        }
                    }
                )//
                .filter(Objects::nonNull)//
                .toList()//
        ;

        LOG.trace("EXIT: createLinks; created={}", created.size());
    }

    @Override
    @Transactional
    public boolean updateProductCount(UUID productId, int newCount) throws ServiceException {
        LOG.trace("ENTER: updateProductCount; productId={{}}, newCount={}", productId, newCount);
        Product product = productDao.getById(productId);
        if (product == null) {
            LOG.error("product not found for productId={{}}", productId);
            throw new ServiceException("Product could not be found for productId={" + productId + "}", Response.Status.NOT_FOUND);
        }

        if (newCount <= 0) {
            LOG.error("product count must be greater than 0 for productId={{}}", productId);
            throw new ServiceException("New count must be greater than zero", Response.Status.BAD_REQUEST);
        }

        product.getProductCount().setProductCount(newCount);
        return true;
    }
}
