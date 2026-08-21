package com.monolito.modularizar.order.internal.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Long id;
    private OrderStatusDomain status;
    private Double total;
    private Long invoiceId;
    private List<OrderLine> lines;
}
