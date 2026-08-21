package com.monolito.modularizar.invoice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoiceRequest {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ISSUED|PAID|CANCELLED", message = "Estado inválido. Valores permitidos: ISSUED, PAID, CANCELLED")
    private String status;
}
