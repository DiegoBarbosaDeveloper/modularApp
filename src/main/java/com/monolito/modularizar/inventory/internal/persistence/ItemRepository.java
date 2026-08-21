package com.monolito.modularizar.inventory.internal.persistence;

import com.monolito.modularizar.inventory.internal.domain.Item;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findBySku(String sku);
}
