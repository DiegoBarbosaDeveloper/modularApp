package com.monolito.modularizar.order.internal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLine {
    private Long itemId;
    private int quantity;
    private Double unitPrice;
}
