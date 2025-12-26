package com.example.lease_management.mapper;

import com.example.lease_management.dto.Customer.CustomerDTO;
import com.example.lease_management.dto.Customer.CustomerRequest;
import com.example.lease_management.entity.Customer;

public class CustomerMapper {
    public static CustomerDTO toDTO(Customer customer){
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        return dto;
    }

    public static Customer toEntity(CustomerRequest req){
        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setPhone(req.getPhone());
        customer.setAddress(req.getAddress());
        return customer;
    }
}
