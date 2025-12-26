package com.example.lease_management.dto.LeaseItem;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

// Used to send response about item on lease.
public record LeaseItemDTO (
        Long id,
        Long itemId,
        String itemName,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalDays,
        BigDecimal dailyRate,
        BigDecimal totalBill,
        Integer quantity
){}

