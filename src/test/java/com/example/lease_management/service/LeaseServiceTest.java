package com.example.lease_management.service;

import com.example.lease_management.Repos.CustomerRepo;
import com.example.lease_management.Repos.ItemRepo;
import com.example.lease_management.dto.Lease.LeaseDTO;
import com.example.lease_management.dto.Lease.LeaseRequest;
import com.example.lease_management.dto.LeaseItem.LeaseItemRequest;
import com.example.lease_management.entity.Customer;
import com.example.lease_management.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LeaseServiceTest {

    @Autowired
    private LeaseService leaseService;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ItemRepo itemRepo;

    private Customer customer;
    private Item mixer;

    private static int count;

    @BeforeEach
    void setup() {
        count +=1;
        customer = customerRepo.save(new Customer(null, "Test User", "999999999"+count, "Delhi"));
        mixer = itemRepo.save(new Item(null, "Concrete Mixer"+count, new BigDecimal("500")));
    }

    @Test
    void sameDayLease_shouldBeOneDayBill() {
        LeaseRequest req = new LeaseRequest(customer.getId(), "Test",
                List.of(new LeaseItemRequest(mixer.getId(), LocalDate.now(), LocalDate.now())));
        LeaseDTO resp = leaseService.create(req);
        assertThat(resp.items().get(0).totalDays()).isEqualTo(1);
        assertThat(resp.items().get(0).totalBill()).isEqualByComparingTo("500");
    }

    @Test
    void thirtyOneDayLease_shouldCalculateCorrectly() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);
        LeaseRequest req = new LeaseRequest(customer.getId(), "Test",
                List.of(new LeaseItemRequest(mixer.getId(), start, end)));
        LeaseDTO resp = leaseService.create(req);
        assertThat(resp.items().get(0).totalDays()).isEqualTo(31);
        assertThat(resp.items().get(0).totalBill()).isEqualByComparingTo("15500");
    }

    @Test
    void invalidRange_shouldThrowException() {
        LocalDate start = LocalDate.of(2025, 1, 10);
        LocalDate end = LocalDate.of(2025, 1, 5);
        LeaseRequest req = new LeaseRequest(customer.getId(), "Test",
                List.of(new LeaseItemRequest(mixer.getId(), start, end)));
        assertThatThrownBy(() -> leaseService.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("endDate must be on or after startDate");
    }
}
