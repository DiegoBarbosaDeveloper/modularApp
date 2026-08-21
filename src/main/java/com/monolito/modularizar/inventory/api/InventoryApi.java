package com.monolito.modularizar.inventory.api;

import com.monolito.modularizar.inventory.internal.persistence.ItemEntity;

public interface InventoryApi {
    ItemEntity getItem(Long itemId);
    ItemEntity adjustStock(Long itemId, int delta);
    ItemEntity createItem(String name, String sku, int stock, double price);
}
