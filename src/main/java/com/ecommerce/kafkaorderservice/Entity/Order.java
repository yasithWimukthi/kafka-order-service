package com.ecommerce.kafkaorderservice.Entity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private UUID id;
    private String customerEmail;
    private long amountCents;
    private String status;
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = "CREATED";
    }

    public Order() {
    }

    public Order(UUID id, String customerEmail, long amountCents, String status, Instant createdAt) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.amountCents = amountCents;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public long getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(long amountCents) {
        this.amountCents = amountCents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}