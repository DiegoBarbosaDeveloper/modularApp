package com.monolito.modularizar.invoice.api;

import com.monolito.modularizar.invoice.api.dto.InvoiceResponse;
import com.monolito.modularizar.invoice.api.dto.UpdateInvoiceRequest;
import java.util.List;

public interface InvoiceApi {

    InvoiceResponse create(double total);

    List<InvoiceResponse> getAll();

    InvoiceResponse getById(Long id);

    InvoiceResponse getByNumber(String number);

    InvoiceResponse update(Long id, UpdateInvoiceRequest request);

    void delete(Long id);
}
