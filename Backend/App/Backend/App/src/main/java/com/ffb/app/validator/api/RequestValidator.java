package com.ffb.app.validator.api;

import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.request.credit.CreditAddRequest;
import com.ffb.model.api.request.credit.CreditHistoryRequest;
import com.ffb.model.exception.ApiException;

import org.eclipse.microprofile.jwt.JsonWebToken;

public interface RequestValidator {
    String validateAndGetLoginNr(JsonWebToken jwt) throws ApiException;

    void validateUpdateRequest(CartItemUpdateRequest request) throws ApiException;

    void validateItemCreationRequest(CartItemCreationRequest request) throws ApiException;

    void validateCreditHistoryRequest(CreditHistoryRequest request) throws ApiException;

    void validateCreditAddRequest(CreditAddRequest request) throws ApiException;
}
