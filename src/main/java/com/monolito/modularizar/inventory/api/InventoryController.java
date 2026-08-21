package com.monolito.modularizar.inventory.api;

import com.monolito.modularizar.inventory.internal.domain.Item;
import com.monolito.modularizar.inventory.internal.persistence.ItemEntity;
import com.monolito.modularizar.inventory.api.dto.AdjustStockRequest;
import com.monolito.modularizar.inventory.api.dto.CreateItemRequest;
import com.monolito.modularizar.inventory.internal.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryApi inventoryApi;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(inventoryApi.getItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryApi.getItem(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateItemRequest request) {

        return ResponseEntity.ok(inventoryApi.createItem(request.getName(), request.getSku(), request.getStock(), request.getPrice()));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> adjustStock(@PathVariable Long id, @Valid @RequestBody AdjustStockRequest request) {
        return ResponseEntity.ok(inventoryApi.adjustStock(id, request.getDelta()));
    }
}
