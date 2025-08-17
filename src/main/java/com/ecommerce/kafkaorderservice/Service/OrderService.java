package com.ecommerce.kafkaorderservice.Service;

import com.ecommerce.kafkaorderservice.DTO.CreateOrderRequest;
import com.ecommerce.kafkaorderservice.Entity.Order;
import com.ecommerce.kafkaorderservice.Event.InventoryResultEvent;
import com.ecommerce.kafkaorderservice.Event.OrderCreatedEvent;
import com.ecommerce.kafkaorderservice.Repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public OrderService(OrderRepository repository,
                        KafkaTemplate<String, String> kafkaTemplate,
                        ObjectMapper objectMapper,
                        @Value("${app.topics.orders}") String topic) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @Transactional
    public UUID createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerEmail(request.customerEmail());
        order.setAmountCents(request.amountCents());
        order.setSku(request.sku());
        order.setQuantity(request.quantity());
        repository.save(order);

        try {
            OrderCreatedEvent evt = new OrderCreatedEvent(
                    "order.created", "v1", order.getId(), order.getSku(), order.getQuantity(),
                    order.getCustomerEmail(), order.getAmountCents(), Instant.now()
            );
            String json = objectMapper.writeValueAsString(evt);
            kafkaTemplate.send(topic, order.getId().toString(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send Kafka event", e);
        }

        return order.getId();
    }

    @KafkaListener(topics = "inventory-events.v1", groupId = "order-service-group")
    public void onInventoryEvent(String payload) throws Exception {
        InventoryResultEvent e = objectMapper.readValue(payload, InventoryResultEvent.class);
        repository.findById(e.orderId()).ifPresent(o -> {
            o.setStatus(e.status()); // CONFIRMED or REJECTED
            repository.save(o);
        });
    }
}