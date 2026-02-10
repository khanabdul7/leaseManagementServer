package com.example.lease_management.Repos;

import com.example.lease_management.entity.Item;
import com.example.lease_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepo extends JpaRepository<Item, Long> {
    boolean existsByNameIgnoreCaseAndUser(String name, User user);

    @Query("""
    SELECT i
    FROM Item i
    WHERE i.deleted = false
      AND i.user = :user
""")
    Page<Item> findAllActive(
            @Param("user") User user,
            Pageable pageable
    );

    @Query("""
    SELECT i
    FROM Item i
    WHERE i.deleted = false
      AND i.user = :user
      AND LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    Page<Item> searchActive(
            @Param("user") User user,
            @Param("name") String name,
            Pageable pageable
    );

    Optional<Item> findByIdAndUserAndDeletedFalse(Long id, User user);

}
