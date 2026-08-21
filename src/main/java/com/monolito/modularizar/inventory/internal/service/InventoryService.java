package com.monolito.modularizar.inventory.internal.service;

import com.monolito.modularizar.inventory.internal.domain.Item;
import com.monolito.modularizar.inventory.internal.persistence.ItemEntity;
import com.monolito.modularizar.inventory.api.InventoryApi;
import com.monolito.modularizar.inventory.internal.persistence.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService implements InventoryApi {

    private final ItemRepository itemRepository;

    public InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemEntity> getItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado: " + itemId));
    }



    @Transactional
    @Override
    public ItemEntity createItem(String name, String sku, int stock, double price) {
        if (itemRepository.findBySku(sku).isPresent()) {
            throw new IllegalArgumentException("Ya existe un item con el SKU: " + sku);
        }
        ItemEntity item = ItemEntity.builder()
            .name(name)
            .sku(sku)
            .stock(stock)
            .price(price)
            .build();
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public ItemEntity adjustStock(Long itemId, int delta) {
        Item item = getItem(itemId);
        int nextStock = item.getStock() - delta;
        if (nextStock <= 0) {
            throw new IllegalArgumentException("No hay suficiente stock para realizar la operación.");
        }

        item.setStock(nextStock);
        return itemRepository.save(item);
    }
}
