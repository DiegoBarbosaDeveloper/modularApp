package com.monolito.modularizar.inventory.internal.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    Optional<ItemEntity> findBySku(String sku);
}
