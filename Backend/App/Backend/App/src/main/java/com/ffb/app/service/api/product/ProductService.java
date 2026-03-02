package com.ffb.app.service.api.product;

import com.ffb.model.api.request.product.ProductLinkRequest;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.api.request.product.ProductRequestSimple;
import com.ffb.model.api.response.product.ProductResponse;
import com.ffb.model.exception.ServiceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponse> listProducts();

    List<ProductResponse> listProductsByLoginNr(String loginNr);

    @Transactional
    ProductResponse createProductByLoginNr(String loginNr, ProductRequestSimple request) throws ServiceException;

    List<ProductResponse> listProductsByFoodCourtId(UUID foodCourtId);

    ProductResponse getProductById(UUID id) throws NotFoundException;

    @Transactional
    ProductResponse createProductWithId(UUID foodCourtId, ProductRequest request) throws ServiceException;

    @Transactional
    void deleteProductById(UUID id) throws NotFoundException, ServiceException;

    @Transactional
    boolean createAssignment(ProductLinkRequest request) throws ServiceException;

    @Transactional
    void deleteAssignmentById(UUID linkId) throws ServiceException;

    @Transactional
    void deleteAssignmentByPair(ProductLinkRequest request) throws ServiceException;

    @Transactional
    void createProducts(UUID id, List<ProductRequest> productRequests);

    @Transactional
    void createLinks(List<ProductLinkRequest> productLinkRequests);

    @Transactional
    boolean updateProductCount(UUID productId, int newCount) throws ServiceException;
}
