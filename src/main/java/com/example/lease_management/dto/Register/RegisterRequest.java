package com.example.lease_management.dto.Register;

public record RegisterRequest(
        String username,
        String email,
        String password
) {}
