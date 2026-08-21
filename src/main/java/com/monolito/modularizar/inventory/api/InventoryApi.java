package com.monolito.modularizar.inventory.api;

import java.util.List;

import com.monolito.modularizar.inventory.internal.domain.Item;
import com.monolito.modularizar.inventory.internal.persistence.ItemEntity;

public interface InventoryApi {
    List<Item> getItems();
    Item getItem(Long itemId);
    Item adjustStock(Long itemId, int delta);
    Item createItem(String name, String sku, int stock, double price);
}
