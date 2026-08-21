package com.monolito.modularizar.order.internal.persistence;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatusPersistence status;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private Long invoiceId;

    @ElementCollection
    @CollectionTable(name = "order_lines", joinColumns = @JoinColumn(name = "order_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "itemId", column = @Column(name = "item_id", nullable = false)),
        @AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false)),
        @AttributeOverride(name = "unitPrice", column = @Column(name = "unit_price", nullable = false))
    })
    private List<OrderLineEntity> lines = new ArrayList<>();
}
