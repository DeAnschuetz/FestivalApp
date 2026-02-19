package com.ffb.model.response.order;

import java.util.List;
import java.util.UUID;

public record OrderItemSimple(UUID productID, String displayName, byte icon, int count, String extra, List<OrderItemSimple> subItems) {

}
