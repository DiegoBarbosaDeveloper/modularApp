package com.monolito.modularizar.service;

import com.monolito.modularizar.domain.Item;
import com.monolito.modularizar.repository.ItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final ItemRepository itemRepository;

    public InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Item no encontrado: " + id));
    }

    @Transactional
    public Item createItem(String name, String sku, int stock, double price) {
        if (itemRepository.findBySku(sku).isPresent()) {
            throw new IllegalArgumentException("Ya existe un item con el SKU: " + sku);
        }
        Item item = Item.builder()
            .name(name)
            .sku(sku)
            .stock(stock)
            .price(price)
            .build();
        return itemRepository.save(item);
    }

    @Transactional
    public Item adjustStock(Long itemId, int delta) {
        Item item = getItem(itemId);
        item.adjustStock(delta);
        return itemRepository.save(item);
    }
}
