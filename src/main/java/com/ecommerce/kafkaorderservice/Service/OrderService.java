package com.ecommerce.kafkaorderservice.Service;

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
    private final OrderRepository repo;
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;
    private final String topic;

    public OrderService(OrderRepository repo, KafkaTemplate<String, String> kafka,
                        ObjectMapper mapper,
                        @Value("${app.topics.orders}") String topic) {
        this.repo = repo;
        this.kafka = kafka;
        this.mapper = mapper;
        this.topic = topic;
    }

    @Transactional
    public UUID create(CreateOrderRequest req) {
        Order o = new Order();
        o.setCustomerEmail(req.customerEmail());
        o.setAmountCents(req.amountCents());
        repo.save(o);

        try {
            String event = mapper.writeValueAsString(o);
            kafka.send(topic, o.getId().toString(), event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return o.getId();
    }
}
