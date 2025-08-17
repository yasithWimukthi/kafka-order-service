package com.ecommerce.kafkaorderservice.Event;

import java.time.Instant;
import java.util.UUID;

// OrderCreatedEvent.java
public record OrderCreatedEvent(
        String type,        // "order.created"
        String version,     // "v1"
        UUID orderId,
        String sku,
        int quantity,
        String customerEmail,
        long amountCents,
        Instant occurredAt
) {}
