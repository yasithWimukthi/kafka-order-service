package com.ecommerce.kafkaorderservice.Service;

import Event.OrderEvent;
import com.ecommerce.kafkaorderservice.DTO.CreateOrderRequest;
import com.ecommerce.kafkaorderservice.Entity.Order;
import com.ecommerce.kafkaorderservice.Repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        repository.save(order);

        try {
            OrderEvent event = new OrderEvent(order.getId(), order.getCustomerEmail(),
                    order.getAmountCents(), order.getStatus());
            String jsonEvent = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, order.getId().toString(), jsonEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send Kafka event", e);
        }

        return order.getId();
    }
}