package com.monolito.modularizar.order.internal.service;

import com.monolito.modularizar.inventory.internal.persistence.ItemEntity;
import com.monolito.modularizar.invoice.api.InvoiceApi;
import com.monolito.modularizar.inventory.api.InventoryApi;
import com.monolito.modularizar.order.api.OrderApi;
import com.monolito.modularizar.order.internal.domain.Order;
import com.monolito.modularizar.order.internal.mapper.OrderMapper;
import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import com.monolito.modularizar.order.internal.persistence.OrderLineEntity;
import com.monolito.modularizar.order.internal.persistence.OrderStatusPersistence;
import com.monolito.modularizar.order.internal.persistence.OrderRepository;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderApi {

    private final OrderRepository orderRepository;
    private final InventoryApi inventoryApi;
    private final InvoiceApi invoiceApi;
    private final OrderMapper orderMapper;


    @Transactional
    @Override
    public Order createOrder(List<String> lineItems) {
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

            ItemEntity item = inventoryApi.getItem(itemId);
            if (item.getStock() < quantity) {
                throw new IllegalArgumentException("No hay stock suficiente para el item " + item.getName());
            }

            total += item.getPrice() * quantity;
            lines.add(new OrderLineEntity(item.getId(), quantity, item.getPrice()));
            inventoryApi.adjustStock(itemId, -quantity);
        }

        var invoiceResponse = invoiceApi.create(total);

        OrderEntity orderEntity = OrderEntity.builder()
            .status(OrderStatusPersistence.PENDING)
            .total(total)
            .invoiceId(invoiceResponse.getId())
            .lines(lines)
            .build();

        return orderMapper.toDomainFromEntity(orderRepository.save(orderEntity));
    }
}
