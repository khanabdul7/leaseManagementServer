package com.example.lease_management.Repos;


import com.example.lease_management.entity.Customer;
import com.example.lease_management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    @Query("""
    SELECT c
    FROM Customer c
    WHERE c.deleted = false
      AND c.user = :user
""")
    Page<Customer> findAllActive(
            @Param("user") User user,
            Pageable pageable
    );


    @Query("""
    SELECT c
    FROM Customer c
    WHERE c.deleted = false
      AND c.user = :user
      AND (
           LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%'))
      )
""")
    Page<Customer> searchActive(
            @Param("user") User user,
            @Param("search") String search,
            Pageable pageable
    );

    Optional<Customer> findByIdAndUserAndDeletedFalse(Long id, User user);

}
