package com.monolito.modularizar;

import static org.junit.jupiter.api.Assertions.*;

import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import com.monolito.modularizar.inventory.internal.domain.Item;
import com.monolito.modularizar.inventory.internal.service.InventoryService;
import com.monolito.modularizar.order.internal.service.OrderService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Test
    void shouldCreateOrderAndInvoice() {
        Item item = inventoryService.createItem("Teclado", "TEC-100", 10, 50.0);

        OrderEntity orderEntity = orderService.createOrder(List.of(item.getId() + ":2"));

        assertEquals(100.0, orderEntity.getTotal());
        assertNotNull(orderEntity.getInvoice());
        assertEquals("PENDING", orderEntity.getStatus().name());
    }
}
