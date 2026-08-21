package com.monolito.modularizar.order.api;

import com.monolito.modularizar.order.internal.domain.Order;

import java.util.List;

public interface OrderApi {
    Order createOrder(List<String> lineItems);
}
