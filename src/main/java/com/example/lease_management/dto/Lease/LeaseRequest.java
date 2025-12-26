package com.example.lease_management.dto.Lease;

import com.example.lease_management.dto.LeaseItem.LeaseItemRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

//Used to save lease record
public record LeaseRequest(
        @NotNull Long customerId,
        @Size(max = 225) String notes,
        @NotEmpty List<LeaseItemRequest> items
) {
}
