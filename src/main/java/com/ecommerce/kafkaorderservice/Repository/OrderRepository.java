package com.ecommerce.kafkaorderservice.Repository;

import com.ecommerce.kafkaorderservice.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> { }