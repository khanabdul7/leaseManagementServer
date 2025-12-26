package com.example.lease_management.Repos;

import com.example.lease_management.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT i FROM Item i WHERE i.deleted = false")
    Page<Item> findAllActive(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.deleted = false AND LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Item> searchActive(@Param("name") String name, Pageable pageable);
}
