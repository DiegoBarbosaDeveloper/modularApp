package com.monolito.modularizar.order.internal.mapper;

import com.monolito.modularizar.invoice.internal.mapper.InvoiceMapper;
import com.monolito.modularizar.order.api.dto.OrderRequest;
import com.monolito.modularizar.order.api.dto.OrderResponse;
import com.monolito.modularizar.order.internal.domain.Order;
import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = InvoiceMapper.class
)
public interface OrderMapper {


    Order toDomainFromRequest(OrderRequest orderRequest);

    @Mapping(target = "invoiceId", source = "invoice.id")
    Order toDomainFromEntity(OrderEntity orderEntity);

    @Mapping(target = "invoice", source = "invoiceId")
    OrderEntity toEntityFromDomain(Order order);


    OrderResponse toResponseFromDomain(Order order);



}
