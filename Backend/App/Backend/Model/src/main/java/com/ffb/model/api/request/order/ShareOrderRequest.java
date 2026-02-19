package com.ffb.model.request.order;

import java.util.UUID;

public record ShareOrderRequest(UUID orderID, String loginNr) {

}
