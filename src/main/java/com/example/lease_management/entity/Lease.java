package com.example.lease_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal grandTotal; //sum of all leaseItems

    @OneToMany(mappedBy = "lease", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaseItem> leaseItems = new ArrayList<>();

    // helper method to add item
    public void addLeaseItem(LeaseItem leaseItem) {
        leaseItems.add(leaseItem);
        leaseItem.setLease(this);
    }

    private String notes;

    @Column(name = "is_deleted")
    private Boolean deleted = false;
}
