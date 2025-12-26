package com.example.lease_management.dto.LeaseItem;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

//Used to save item on lease
public record LeaseItemRequest(
        Long leaseItemId,
       @NotNull Long itemId,
       @NotNull(message = "startDate should not be empty !") LocalDate startDate,
       @NotNull(message = "endDate should not be empty !") LocalDate endDate,
        @NotNull(message = "pricePerDay should not be empty!") BigDecimal pricePerDay,
        @NotNull(message = "quantity should not be empty!") Integer quantity
) {}
