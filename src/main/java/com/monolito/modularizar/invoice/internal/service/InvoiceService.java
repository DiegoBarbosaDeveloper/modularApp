package com.monolito.modularizar.invoice.internal.service;

import com.monolito.modularizar.invoice.api.InvoiceApi;
import com.monolito.modularizar.invoice.api.dto.InvoiceResponse;
import com.monolito.modularizar.invoice.api.dto.UpdateInvoiceRequest;
import com.monolito.modularizar.invoice.internal.domain.Invoice;
import com.monolito.modularizar.invoice.internal.domain.InvoiceStatus;
import com.monolito.modularizar.invoice.internal.entity.InvoiceEntity;
import com.monolito.modularizar.invoice.internal.mapper.InvoiceMapper;
import com.monolito.modularizar.invoice.internal.repository.InvoiceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService implements InvoiceApi {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    @Transactional
    public InvoiceResponse create(double total) {
        long next = invoiceRepository.count() + 1;
        Invoice invoice = Invoice.builder()
            .number(String.format("FAC-%06d", next))
            .total(total)
            .issuedAt(LocalDateTime.now())
            .status(InvoiceStatus.ISSUED)
            .build();

        InvoiceEntity saved = invoiceRepository.save(invoiceMapper.toEntity(invoice));
        return invoiceMapper.toResponse(invoiceMapper.toDomain(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAll() {
        return invoiceRepository.findAll().stream()
            .map(invoiceMapper::toDomain)
            .map(invoiceMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long id) {
        return invoiceRepository.findById(id)
            .map(invoiceMapper::toDomain)
            .map(invoiceMapper::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getByNumber(String number) {
        return invoiceRepository.findByNumber(number)
            .map(invoiceMapper::toDomain)
            .map(invoiceMapper::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + number));
    }

    @Override
    @Transactional
    public InvoiceResponse update(Long id, UpdateInvoiceRequest request) {
        InvoiceEntity entity = invoiceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + id));

        if (entity.getStatus() == InvoiceStatus.CANCELLED) {
            throw new IllegalArgumentException(
                "No se puede modificar una factura cancelada: " + entity.getNumber());
        }

        entity.setStatus(InvoiceStatus.valueOf(request.getStatus()));
        InvoiceEntity saved = invoiceRepository.save(entity);
        return invoiceMapper.toResponse(invoiceMapper.toDomain(saved));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Factura no encontrada: " + id);
        }
        invoiceRepository.deleteById(id);
    }
}
