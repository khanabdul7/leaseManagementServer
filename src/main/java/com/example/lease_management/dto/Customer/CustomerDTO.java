package com.example.lease_management.dto.Customer;

import lombok.Getter;
import lombok.Setter;

//Class used for Output response
@Getter @Setter
public class CustomerDTO {
    private Long id;
    private String name;
    private String phone;
    private String address;
}
