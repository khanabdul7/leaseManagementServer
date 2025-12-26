package com.example.lease_management.dto.Item;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank String name,
        @DecimalMin(value = "0.01", message = "Price must be positive !")BigDecimal pricePerDay
        ) {}
