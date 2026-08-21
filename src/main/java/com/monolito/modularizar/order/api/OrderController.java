package com.monolito.modularizar.order.api;

import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import com.monolito.modularizar.order.internal.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApi orderApi;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody List<String> lines) {
        return ResponseEntity.ok(orderApi.createOrder(lines));
    }
}
