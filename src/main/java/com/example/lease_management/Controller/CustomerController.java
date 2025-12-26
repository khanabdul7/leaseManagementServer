package com.example.lease_management.Controller;

import com.example.lease_management.dto.Customer.CustomerDTO;
import com.example.lease_management.dto.Customer.CustomerRequest;
import com.example.lease_management.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service){
        this.service = service;
    }

    @GetMapping
    public Page<CustomerDTO> list(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
    ){
        return service.list(search, pageable);
    }

    @PostMapping
    public CustomerDTO create(@RequestBody CustomerRequest customerReq){
        return service.create(customerReq);
    }

    @PutMapping("/{id}")
    public CustomerDTO update( @PathVariable Long id, @RequestBody CustomerRequest customerReq){
        return service.update(id, customerReq);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
