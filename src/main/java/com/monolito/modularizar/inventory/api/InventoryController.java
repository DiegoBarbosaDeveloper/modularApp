package com.monolito.modularizar.inventory.api;

import com.monolito.modularizar.inventory.internal.domain.Item;
import com.monolito.modularizar.inventory.api.dto.AdjustStockRequest;
import com.monolito.modularizar.inventory.api.dto.CreateItemRequest;
import com.monolito.modularizar.inventory.internal.service.InventoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Item> findAll() {
        return inventoryService.getItems();
    }

    @GetMapping("/{id}")
    public Item findById(@PathVariable Long id) {
        return inventoryService.getItem(id);
    }

    @PostMapping
    public ResponseEntity<Item> create(@Valid @RequestBody CreateItemRequest request) {
        Item created = inventoryService.createItem(
            request.getName(),
            request.getSku(),
            request.getStock(),
            request.getPrice()
        );
        return ResponseEntity.ok(created);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Item> adjustStock(@PathVariable Long id, @Valid @RequestBody AdjustStockRequest request) {
        return ResponseEntity.ok(inventoryService.adjustStock(id, request.getDelta()));
    }
}
