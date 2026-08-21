package com.monolito.modularizar.inventory.internal.mapper;

import com.monolito.modularizar.inventory.internal.domain.Item;
import com.monolito.modularizar.inventory.internal.persistence.ItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toDomain(ItemEntity itemEntity);
    ItemEntity toEntity(Item item);

}
