package com.ffb.model.api.request.order;

import java.util.UUID;

public record ShareOrderRequest(UUID orderID, String loginNr) {

}
