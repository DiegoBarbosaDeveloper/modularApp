package com.monolito.modularizar.order.api.dto;

import com.monolito.modularizar.order.internal.domain.OrderLine;
import com.monolito.modularizar.order.internal.domain.OrderStatusDomain;

import java.util.List;

public record OrderResponse(
        OrderStatusDomain status,
        Double total,
        Long invoiceId,
        List<OrderLine> lines
) {
}
