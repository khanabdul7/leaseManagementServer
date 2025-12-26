package com.example.lease_management.service;

import com.example.lease_management.Repos.CustomerRepo;
import com.example.lease_management.dto.Customer.CustomerDTO;
import com.example.lease_management.dto.Customer.CustomerRequest;
import com.example.lease_management.entity.Customer;
import com.example.lease_management.mapper.CustomerMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepo repo;

    public CustomerService(CustomerRepo repo){
        this.repo = repo;
    }

    public CustomerDTO create(CustomerRequest customerReq){
        Customer customer = CustomerMapper.toEntity(customerReq);
        return CustomerMapper.toDTO(repo.save(customer));
    }

    public Page<CustomerDTO> list(String search, Pageable pageable){
        Page<Customer> page;
        if(search == null || search.isBlank()){
            page = repo.findAllActive(pageable);
        }else{
            page = repo.searchActive(search, pageable);
        }
        return page.map(p->CustomerMapper.toDTO(p));
    }

    public CustomerDTO update(Long id, CustomerRequest customerReq){
        Customer searchedCustomer = repo.findById(id).orElseThrow(()-> new RuntimeException("Customer not found !"));
        searchedCustomer.setName(customerReq.getName());
        searchedCustomer.setPhone(customerReq.getPhone());
        searchedCustomer.setAddress(customerReq.getAddress());
        return CustomerMapper.toDTO(repo.save(searchedCustomer));
    }

    @Transactional
    //softDelete
    public void delete(Long id){
        Customer customer = repo.findById(id).orElseThrow();
        customer.setDeleted(true);
        repo.save(customer);
    }
}
