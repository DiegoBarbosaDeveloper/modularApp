package com.monolito.modularizar.repository;

import com.monolito.modularizar.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
