package com.example.lease_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String name;

    @DecimalMin(value = "0.01", message = "Price must be positive !")
    @Column(nullable = false, name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "is_deleted")
    private Boolean deleted = false;
}
