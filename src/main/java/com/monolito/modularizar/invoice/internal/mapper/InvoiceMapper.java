package com.monolito.modularizar.invoice.internal.mapper;

import com.monolito.modularizar.invoice.api.dto.InvoiceResponse;
import com.monolito.modularizar.invoice.internal.domain.Invoice;
import com.monolito.modularizar.invoice.internal.entity.InvoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceEntity toEntity(Invoice invoice);

    Invoice toDomain(InvoiceEntity entity);

    InvoiceResponse toResponse(Invoice invoice);

    default InvoiceEntity fromId(Long id) {
        if (id == null) {
            return null;
        }

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(id);
        return invoice;
    }

    default Long toId(InvoiceEntity invoice) {
        return invoice != null ? invoice.getId() : null;
    }
}
