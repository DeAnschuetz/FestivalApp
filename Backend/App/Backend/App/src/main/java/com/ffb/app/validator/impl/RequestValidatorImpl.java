package com.ffb.app.validator.impl;

import com.ffb.app.validator.api.RequestValidator;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.request.credit.CreditAddRequest;
import com.ffb.model.api.request.credit.CreditHistoryRequest;
import com.ffb.model.exception.ApiException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class RequestValidatorImpl implements RequestValidator {

    private static final Logger LOG = LoggerFactory.getLogger(RequestValidator.class);

    @Override
    public String validateAndGetLoginNr(JsonWebToken jwt) throws ApiException {
        String loginNr = jwt.getName();
        checkCondition(loginNr == null || loginNr.isBlank(), "loginNr is null/blank", "LoginNr must be provided", Response.Status.UNAUTHORIZED);
        return loginNr;
    }

    @Override
    public void validateUpdateRequest(CartItemUpdateRequest request) throws ApiException {
        checkCondition(request == null, "request is null", "Invalid Request", Response.Status.BAD_REQUEST);
        assert request != null;
        checkCondition(request.cartItemId() == null, "cartItemId is null", "Cart Item ID must be provided.", Response.Status.BAD_REQUEST);
        checkCondition(request.itemCount() <= 0, "itemCount is <= 0", "Item Count must be greater than 0.", Response.Status.BAD_REQUEST);
        checkCondition(request.extra() != null && request.extra().length() > 255, "extra was too long", "Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
    }

    @Override
    public void validateItemCreationRequest(CartItemCreationRequest request) throws ApiException {
        checkCondition(request == null, "request is null", "Invalid Request", Response.Status.BAD_REQUEST);
        assert request != null;
        checkCondition(request.productId() == null, "productId is null", "Product ID must be provided.", Response.Status.BAD_REQUEST);
        checkCondition(request.itemCount() <= 0, "itemCount is <= 0", "Item Count must be greater than 0.", Response.Status.BAD_REQUEST);
        checkCondition(request.extra() != null && request.extra().length() > 255, "extra is too long", "Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
    }

    @Override
    public void validateCreditHistoryRequest(CreditHistoryRequest request) throws ApiException {
        checkCondition(request == null, "request is null", "Invalid Request", Response.Status.BAD_REQUEST);
        assert request != null;
        checkCondition(request.loginNr() == null || request.loginNr().isBlank(), "loginNr is null or empty", "Login Nr is required.", Response.Status.BAD_REQUEST);
        checkCondition(request.pageIndex() <= 0, "pageIndex was <= 0", "Page Index is required.", Response.Status.BAD_REQUEST);
        checkCondition(request.pageSize() <= 0, "pageSize was <= 0", "Page Size is required.", Response.Status.BAD_REQUEST);
    }

    @Override
    public void validateCreditAddRequest(CreditAddRequest request) throws ApiException {
        checkCondition(request == null, "request is null", "Invalid Request", Response.Status.BAD_REQUEST);
        assert request != null;
        checkCondition(request.amount() <= 0, "amount is <= 0", "The amount must be greater than 0.", Response.Status.BAD_REQUEST);
    }

    private static void checkCondition(boolean condition, String failedLogStatement, String failedExceptionMessage, Response.Status status) throws ApiException {
        if (condition) {
            LOG.error(failedLogStatement);
            throw new ApiException(failedExceptionMessage, status);
        }
    }
}
