package com.monolito.modularizar.order.internal.service;

import com.monolito.modularizar.domain.Invoice;
import com.monolito.modularizar.domain.Item;
import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import com.monolito.modularizar.order.internal.persistence.OrderLineEntity;
import com.monolito.modularizar.order.internal.persistence.OrderStatusPersistence;
import com.monolito.modularizar.order.internal.persistence.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.monolito.modularizar.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public OrderService(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public OrderEntity createOrder(List<String> lineItems) {
        List<OrderLineEntity> lines = new ArrayList<>();
        double total = 0.0;

        for (String rawLine : lineItems) {
            String[] parts = rawLine.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Formato inválido de línea: " + rawLine);
            }

            Long itemId = Long.parseLong(parts[0]);
            int quantity = Integer.parseInt(parts[1]);
            if (quantity <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser positiva.");
            }

            Item item = inventoryService.getItem(itemId);
            if (item.getStock() < quantity) {
                throw new IllegalArgumentException("No hay stock suficiente para el item " + item.getName());
            }

            total += item.getPrice() * quantity;
            lines.add(new OrderLineEntity(item.getId(), quantity, item.getPrice()));
            inventoryService.adjustStock(itemId, -quantity);
        }

        Invoice invoice = Invoice.builder()
            .number("FAC-" + System.currentTimeMillis())
            .total(total)
            .issuedAt(LocalDateTime.now())
            .build();

        OrderEntity orderEntity = OrderEntity.builder()
            .status(OrderStatusPersistence.PENDING)
            .total(total)
            .invoice(invoice)
            .lines(lines)
            .build();

        return orderRepository.save(orderEntity);
    }
}
