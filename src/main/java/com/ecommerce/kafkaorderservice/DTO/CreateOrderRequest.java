package com.ecommerce.kafkaorderservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotBlank String customerEmail,
        @Positive long amountCents
) { }
