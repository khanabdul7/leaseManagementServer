package com.example.lease_management.service;

import com.example.lease_management.Repos.CustomerRepo;
import com.example.lease_management.dto.Customer.CustomerDTO;
import com.example.lease_management.dto.Customer.CustomerRequest;
import com.example.lease_management.entity.Customer;
import com.example.lease_management.entity.User;
import com.example.lease_management.mapper.CustomerMapper;
import com.example.lease_management.security.CurrentUserProvider;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepo repo;
    private final CurrentUserProvider currentUserProvider;

    public CustomerService(CustomerRepo repo, CurrentUserProvider currentUserProvider){
        this.repo = repo;
        this.currentUserProvider = currentUserProvider;
    }

    public CustomerDTO create(CustomerRequest customerReq){
        User user = currentUserProvider.get();
        Customer customer = CustomerMapper.toEntity(customerReq);
        customer.setUser(user); //Enforce Ownership
        return CustomerMapper.toDTO(repo.save(customer));
    }

    public Page<CustomerDTO> list(String search, Pageable pageable){
        Page<Customer> page;
        User user = currentUserProvider.get();
        if(search == null || search.isBlank()){
            page = repo.findAllActive(user, pageable);
        }else{
            page = repo.searchActive(user, search, pageable);
        }
        return page.map(p->CustomerMapper.toDTO(p));
    }

    public CustomerDTO update(Long id, CustomerRequest customerReq){
        User user = currentUserProvider.get();
        Customer searchedCustomer = repo.findByIdAndUserAndDeletedFalse(id, user).orElseThrow(()-> new RuntimeException("Customer not found !"));
        searchedCustomer.setName(customerReq.getName());
        searchedCustomer.setPhone(customerReq.getPhone());
        searchedCustomer.setAddress(customerReq.getAddress());
        return CustomerMapper.toDTO(repo.save(searchedCustomer));
    }

    @Transactional
    //softDelete
    public void delete(Long id){
        User user = currentUserProvider.get();
        Customer customer = repo.findByIdAndUserAndDeletedFalse(id, user).orElseThrow();
        customer.setDeleted(true);
        repo.save(customer);
    }
}
