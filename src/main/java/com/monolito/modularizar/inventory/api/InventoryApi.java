package com.monolito.modularizar.inventory.api;

import com.monolito.modularizar.inventory.internal.domain.Item;

public interface InventoryApi {
    Item getItem(Long itemId);
    Item adjustStock(Long itemId, int delta);
    Item createItem(String name, String sku, int stock, double price);
}
