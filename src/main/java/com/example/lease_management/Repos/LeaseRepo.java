package com.example.lease_management.Repos;

import com.example.lease_management.entity.Lease;
import com.example.lease_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaseRepo extends JpaRepository<Lease, Long> {

    @Query("SELECT l FROM Lease l WHERE l.deleted = false AND l.id = :id AND l.user = :user")
    Optional<Lease> findActiveById(@Param("id") Long id, @Param("user") User user);

    @Query("""
    SELECT l FROM Lease l
    WHERE l.deleted = false
      AND l.user = :user
      AND (:customerName IS NULL OR LOWER(l.customer.name) LIKE LOWER(CONCAT('%', :customerName, '%')))
""")
    List<Lease> searchActiveLeases(@Param("user") User user, @Param("customerName") String customerName);


    Optional<Lease> findByIdAndUser(Long id, User user);
}
