package com.thai.order_service.repository;

import com.thai.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUserId(String userId);
}
