package com.example.lease_management.Repos;

import com.example.lease_management.entity.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaseRepo extends JpaRepository<Lease, Long> {

    @Query("SELECT l FROM Lease l WHERE l.deleted = false AND l.id = :id")
    Optional<Lease> findActiveById(@Param("id") Long id);

    @Query("""
    SELECT l FROM Lease l
    WHERE l.deleted = false
      AND (:customerName IS NULL OR LOWER(l.customer.name) LIKE LOWER(CONCAT('%', :customerName, '%')))
""")
    List<Lease> searchActiveLeases(@Param("customerName") String customerName);


}
