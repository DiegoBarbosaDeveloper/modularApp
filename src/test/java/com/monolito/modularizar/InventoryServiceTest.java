package com.monolito.modularizar;

import static org.junit.jupiter.api.Assertions.*;

import com.monolito.modularizar.domain.Item;
import com.monolito.modularizar.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    void shouldCreateAndReduceStock() {
        Item item = inventoryService.createItem("Mouse", "MOU-001", 10, 25.0);

        Item updated = inventoryService.adjustStock(item.getId(), -3);

        assertEquals(7, updated.getStock());
        assertEquals("Mouse", updated.getName());
        assertEquals("Mouse", inventoryService.getItem(item.getId()).getName());
    }
}
