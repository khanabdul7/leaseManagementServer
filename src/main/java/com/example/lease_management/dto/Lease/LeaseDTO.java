package com.example.lease_management.dto.Lease;

import com.example.lease_management.dto.LeaseItem.LeaseItemDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//Used to send back (response) to lease (Final calculated bill of all leased items)
public record LeaseDTO(
   Long id,
   Long customerId,
   String customerName,
   BigDecimal grandTotal,
   String notes,
   List<LeaseItemDTO> items,
   LocalDateTime createdAt,
   LocalDateTime updatedAt
) {}
