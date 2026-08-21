package com.monolito.modularizar.invoice.internal.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    private Long id;

    private String number;

    private double total;

    private LocalDateTime issuedAt;

    private InvoiceStatus status;
}
