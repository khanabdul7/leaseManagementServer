package com.example.lease_management.Repos;

import com.example.lease_management.entity.LeaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaseItemRepo extends JpaRepository<LeaseItem, Long> {

}
