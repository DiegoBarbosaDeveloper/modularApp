package com.monolito.modularizar.repository;

import com.monolito.modularizar.domain.Invoice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByNumber(String number);
}
