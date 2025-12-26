package com.example.lease_management.dto.Item;

import java.math.BigDecimal;

public record ItemDTO(Long id, String name, BigDecimal pricePerDay) {}