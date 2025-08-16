package com.ecommerce.kafkaorderservice.Controller;

import com.ecommerce.kafkaorderservice.DTO.CreateOrderRequest;
import com.ecommerce.kafkaorderservice.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateOrderRequest req) {
        UUID id = service.createOrder(req);
        return ResponseEntity.created(URI.create("/api/orders/" + id)).body(id);
    }
}
