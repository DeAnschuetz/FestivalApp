package com.ffb.model.api.response.food.court;

import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.product.ProductResponse;

import java.util.List;

public record FoodCourtResponseFull(FoodCourtResponse foodCourt, List<ProductResponse> products, List<FoodOrderResponse> orders) {
}
