package com.example.lease_management.service;

import com.example.lease_management.Repos.CustomerRepo;
import com.example.lease_management.Repos.ItemRepo;
import com.example.lease_management.Repos.LeaseItemRepo;
import com.example.lease_management.Repos.LeaseRepo;
import com.example.lease_management.dto.Customer.CustomerRequest;
import com.example.lease_management.dto.Item.ItemRequest;
import com.example.lease_management.dto.Lease.LeaseDTO;
import com.example.lease_management.dto.Lease.LeaseRequest;
import com.example.lease_management.dto.LeaseItem.LeaseItemDTO;
import com.example.lease_management.dto.LeaseItem.LeaseItemRequest;
import com.example.lease_management.entity.*;
import com.example.lease_management.security.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

//Used to calculate Final bill of leased items.
@Service
public class
LeaseService {

    private final LeaseRepo leaseRepo;
    private final LeaseItemRepo leaseItemRepo;
    private final ItemRepo itemRepo;
    private final CustomerRepo customerRepo;
    private final CurrentUserProvider currentUserProvider;

    public LeaseService(
            LeaseRepo leaseRepo, LeaseItemRepo leaseItemRepo, ItemRepo itemRepo, CustomerRepo customerRepo, CurrentUserProvider currentUserProvider
    ){
        this.leaseRepo = leaseRepo;
        this.leaseItemRepo = leaseItemRepo;
        this.customerRepo = customerRepo;
        this.itemRepo = itemRepo;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    // we need to save customer, item details as lease record
    public LeaseDTO create (LeaseRequest leaseRequest){
        User user = currentUserProvider.get();
        Customer customer = customerRepo.findByIdAndUserAndDeletedFalse(leaseRequest.customerId(), user)
                .orElseThrow(()-> new EntityNotFoundException("Customer not found !"));

        Lease lease = new Lease();
        lease.setCustomer(customer);
        lease.setNotes(leaseRequest.notes());
        lease.setUser(user);

        BigDecimal grandTotal = BigDecimal.ZERO;

        //checking items, if they exist then create leaseItem record
        for(LeaseItemRequest leaseItemRequest : leaseRequest.items()){
           Item item = itemRepo.findByIdAndUserAndDeletedFalse(leaseItemRequest.itemId(), user)
                    .orElseThrow(()-> new EntityNotFoundException("LeaseItem not found for id: "+leaseItemRequest.itemId()));

           //Calculate totalDays based on leaseItemRequest data.
            long totalDays = ChronoUnit.DAYS.between(leaseItemRequest.startDate(), leaseItemRequest.endDate()) +1;

            if(totalDays < 0){
                throw new IllegalArgumentException("endDate must be on or after startDate");
            }

            //Calculating totalBill of an item.
            BigDecimal dailyRate = leaseItemRequest.pricePerDay();
            BigDecimal totalBill = dailyRate.multiply(BigDecimal.valueOf(totalDays));

            //Preparing leaseItem
            LeaseItem leaseItem = new LeaseItem();
            leaseItem.setLease(lease);
            leaseItem.setItem(item);
            leaseItem.setStartDate(leaseItemRequest.startDate());
            leaseItem.setEndDate(leaseItemRequest.endDate());
            leaseItem.setDailyRateSnapshot(leaseItemRequest.pricePerDay());
            leaseItem.setTotalDays((int) totalDays);
            leaseItem.setTotalBill(totalBill);
            leaseItem.setUser(user);

            lease.addLeaseItem(leaseItem); //adding leasedItem record in lease
            grandTotal = grandTotal.add(totalBill); //adding total of a leasedItem
        }

        lease.setGrandTotal(grandTotal);
        Lease savedLeaseRecord = leaseRepo.save(lease);
        return toDTO(savedLeaseRecord);
    }

    public LeaseDTO getLease(Long id){
        User user = currentUserProvider.get();
        Lease lease = leaseRepo.findActiveById(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Lease record not found or deleted!"));
        return toDTO(lease);
    }

    public List<LeaseDTO> getList(LocalDate from, LocalDate to, String customerName){
        User user = currentUserProvider.get();
        List<Lease> leaseList = leaseRepo.searchActiveLeases(user, customerName);

        // filter by dates in Java
        if (from != null || to != null) {
            leaseList = leaseList.stream()
                    .filter(l -> l.getLeaseItems().stream().anyMatch(it -> {
                        boolean afterFrom = (from == null || !it.getStartDate().isBefore(from));
                        boolean beforeTo  = (to == null || !it.getEndDate().isAfter(to));
                        return afterFrom && beforeTo;
                    }))
                    .toList();
        }
        return leaseList.stream().map(LeaseService::toDTO).toList();
    }

        public LeaseDTO updateLease(LeaseRequest req, Long id){
            User user = currentUserProvider.get();
            Lease existedLease = leaseRepo.findByIdAndUser(id, user).orElseThrow(()-> new EntityNotFoundException("Lease Record not found for id: "+id));

            if(req.notes() != null){
                existedLease.setNotes(req.notes());
            }

            if(req.items() != null){
                BigDecimal newGrandTotal = BigDecimal.ZERO;

                for(LeaseItemRequest itemRequest: req.items()){
                    LeaseItem li;
                    if(itemRequest.leaseItemId() != null){
                        li = existedLease.getLeaseItems().stream()
                                .filter(existing -> existing.getItem().getId().equals(itemRequest.itemId()))
                                 .findFirst()
                                .orElseThrow(() -> new EntityNotFoundException("LeaseItem not found for itemId: " + itemRequest.itemId()));
                    }else{
                        // New LeaseItem → create fresh
                        li = new LeaseItem();
                        li.setLease(existedLease);
                        Item item = itemRepo.findByIdAndUserAndDeletedFalse(itemRequest.itemId(), user)
                                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

                        li.setItem(item);
                        li.setUser(user);
                        li.setDailyRateSnapshot(item.getPricePerDay());

                        existedLease.getLeaseItems().add(li);
                    }

                    long totalDays = ChronoUnit.DAYS.between(itemRequest.startDate(), itemRequest.endDate()) + 1;
                    if(totalDays <= 0){
                        throw new IllegalArgumentException("endDate should be greater or equals to startDate !");
                    }

                    // ✅ ensure rate is available
                    BigDecimal dailyRate = itemRequest.pricePerDay();
                    BigDecimal totalBill = dailyRate.multiply(BigDecimal.valueOf(totalDays).multiply(BigDecimal.valueOf(itemRequest.quantity())));

                    li.setStartDate(itemRequest.startDate());
                    li.setEndDate(itemRequest.endDate());
                    li.setTotalDays((int) totalDays);
                    li.setTotalBill(totalBill);
                    li.setQuantity(itemRequest.quantity());
                    li.setDailyRateSnapshot(itemRequest.pricePerDay());

                    newGrandTotal = newGrandTotal.add(totalBill);
                }
                existedLease.setGrandTotal(newGrandTotal);
            }
            return toDTO(leaseRepo.save(existedLease));
        }

    @Transactional
    public void softDeleteLease(Long id) {
        User user = currentUserProvider.get();
        Lease lease = leaseRepo.findByIdAndUser(id, user).orElseThrow();
        lease.setDeleted(true);
        leaseRepo.save(lease);
    }


    //Mapper utility Methods...

    private static LeaseDTO toDTO(Lease lease){
        List<LeaseItemDTO> leaseItemDTOList = lease.getLeaseItems().stream()
                .map(li ->
                        new LeaseItemDTO(
                                li.getId(),
                                li.getItem().getId(),
                                li.getItem().getName(),
                                li.getStartDate(),
                                li.getEndDate(),
                                li.getTotalDays(),
                                li.getDailyRateSnapshot(),
                                li.getTotalBill(),
                                li.getQuantity()
                        )).toList();

        return new LeaseDTO(
                lease.getId(),
                lease.getCustomer().getId(),
                lease.getCustomer().getName(),
                lease.getGrandTotal(),
                lease.getNotes(),
                leaseItemDTOList,
                lease.getCreatedAt(),
                lease.getUpdatedAt()
        );
    }

}
