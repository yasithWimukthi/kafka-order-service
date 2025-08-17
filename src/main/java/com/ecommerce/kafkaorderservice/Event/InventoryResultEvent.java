package com.ecommerce.kafkaorderservice.Event;

import java.time.Instant;
import java.util.UUID;

// InventoryResultEvent.java
public record InventoryResultEvent(
        String type, String version, UUID orderId, String sku, int quantity,
        String status, String reason, Instant occurredAt
) {}