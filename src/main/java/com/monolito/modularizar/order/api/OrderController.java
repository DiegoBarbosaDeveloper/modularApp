package com.monolito.modularizar.order.api;

import com.monolito.modularizar.order.internal.persistence.OrderEntity;
import com.monolito.modularizar.order.internal.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderEntity> create(@Valid @RequestBody List<String> lines) {
        OrderEntity orderEntity = orderService.createOrder(lines);
        return ResponseEntity.ok(orderEntity);
    }
}
