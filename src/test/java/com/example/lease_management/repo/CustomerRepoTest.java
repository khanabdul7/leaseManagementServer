package com.example.lease_management.repo;

import com.example.lease_management.Repos.CustomerRepo;
import com.example.lease_management.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepoTest {

    @Autowired
    private CustomerRepo repo;

    @Test
    void testFindByPhoneContainingIgnoreCase(){
        // Arrange
        Customer c1 = new Customer(null, "Amit", "9876543210", "Delhi");
        Customer c2 = new Customer(null, "Sunil", "9812345678", "Mumbai");
        repo.save(c1);
        repo.save(c2);

        // Act
        Page<Customer> results = repo.findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase("Amit", "987", PageRequest.of(0,10));

        // Assert
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().getFirst().getName()).isEqualTo("Amit");
        assertThat(results.getContent().getFirst().getPhone()).isEqualTo("9876543210");
    }
}
