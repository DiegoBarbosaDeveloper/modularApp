package com.monolito.modularizar.order.internal.service;

import com.monolito.modularizar.domain.Item;

public interface InventoryApi {
    Item getItem(Long itemId);
    Item adjustStock(Long itemId, int delta);
    Item createItem(String name, String sku, int stock, double price);
}
