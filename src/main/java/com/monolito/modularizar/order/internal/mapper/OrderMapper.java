package com.monolito.modularizar.order.internal.mapper;

import com.monolito.modularizar.order.api.dto.OrderRequest;
import com.monolito.modularizar.order.api.dto.OrderResponse;
import com.monolito.modularizar.order.internal.domain.Order;
import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring"
)
public interface OrderMapper {


    Order toDomainFromRequest(OrderRequest orderRequest);

    Order toDomainFromEntity(OrderEntity orderEntity);

    OrderEntity toEntityFromDomain(Order order);


    OrderResponse toResponseFromDomain(Order order);



}
