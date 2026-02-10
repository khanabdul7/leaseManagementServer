package com.example.lease_management.Repos;

import com.example.lease_management.entity.Lease;
import com.example.lease_management.entity.LeaseItem;
import com.example.lease_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaseItemRepo extends JpaRepository<LeaseItem, Long> {

    List<LeaseItem> findByUser(User user);

    List<LeaseItem> findByLeaseAndUser(Lease lease, User user);
}
