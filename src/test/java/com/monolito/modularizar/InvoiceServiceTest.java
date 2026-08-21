package com.monolito.modularizar;

import static org.junit.jupiter.api.Assertions.*;

import com.monolito.modularizar.invoice.api.InvoiceApi;
import com.monolito.modularizar.invoice.api.dto.InvoiceResponse;
import com.monolito.modularizar.invoice.api.dto.UpdateInvoiceRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvoiceServiceTest {

    @Autowired
    private InvoiceApi invoiceApi;

    @Test
    void shouldPerformFullCrud() {
        InvoiceResponse created = invoiceApi.create(250.0);

        assertNotNull(created.getId());
        assertTrue(created.getNumber().startsWith("FAC-"));
        assertEquals(250.0, created.getTotal());
        assertEquals("ISSUED", created.getStatus());

        InvoiceResponse byNumber = invoiceApi.getByNumber(created.getNumber());
        assertEquals(created.getId(), byNumber.getId());

        List<InvoiceResponse> all = invoiceApi.getAll();
        assertTrue(all.stream().anyMatch(i -> i.getId().equals(created.getId())));

        InvoiceResponse paid = invoiceApi.update(created.getId(), new UpdateInvoiceRequest("PAID"));
        assertEquals("PAID", paid.getStatus());

        invoiceApi.delete(created.getId());
        assertFalse(invoiceApi.getAll().stream().anyMatch(i -> i.getId().equals(created.getId())));
    }

    @Test
    void shouldNotModifyCancelledInvoice() {
        InvoiceResponse created = invoiceApi.create(100.0);
        invoiceApi.update(created.getId(), new UpdateInvoiceRequest("CANCELLED"));

        assertThrows(IllegalArgumentException.class,
            () -> invoiceApi.update(created.getId(), new UpdateInvoiceRequest("PAID")));
    }
}
