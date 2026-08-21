package com.monolito.modularizar.invoice.internal.repository;

import com.monolito.modularizar.invoice.internal.entity.InvoiceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByNumber(String number);
}
