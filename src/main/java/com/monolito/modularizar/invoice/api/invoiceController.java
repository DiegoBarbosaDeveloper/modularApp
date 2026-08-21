package com.monolito.modularizar.invoice.api;

import com.monolito.modularizar.invoice.api.dto.CreateInvoiceRequest;
import com.monolito.modularizar.invoice.api.dto.InvoiceResponse;
import com.monolito.modularizar.invoice.api.dto.UpdateInvoiceRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceApi invoiceApi;

    public InvoiceController(InvoiceApi invoiceApi) {
        this.invoiceApi = invoiceApi;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceResponse created = invoiceApi.create(request.getTotal());
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<InvoiceResponse> getAll() {
        return invoiceApi.getAll();
    }

    @GetMapping("/{id}")
    public InvoiceResponse getById(@PathVariable Long id) {
        return invoiceApi.getById(id);
    }

    @GetMapping("/number/{number}")
    public InvoiceResponse getByNumber(@PathVariable String number) {
        return invoiceApi.getByNumber(number);
    }

    @PatchMapping("/{id}/status")
    public InvoiceResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateInvoiceRequest request) {
        return invoiceApi.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invoiceApi.delete(id);
        return ResponseEntity.noContent().build();
    }
}
