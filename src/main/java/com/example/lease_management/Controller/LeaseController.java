package com.example.lease_management.Controller;

import com.example.lease_management.dto.Lease.LeaseDTO;
import com.example.lease_management.dto.Lease.LeaseRequest;
import com.example.lease_management.service.LeaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lease")
public class LeaseController {

    private final LeaseService service;

    public LeaseController(LeaseService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LeaseDTO> createLeaseRecord(@RequestBody @Valid LeaseRequest req){
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaseDTO> getLeaseRecord(@PathVariable Long id){
        return ResponseEntity.ok(service.getLease(id));
    }

    @GetMapping
    public ResponseEntity<List<LeaseDTO>> getLeaseList(@RequestParam(required = false) LocalDate from,
                                                       @RequestParam(required = false) LocalDate to,
                                                       @RequestParam(required = false) String customerName){
       return ResponseEntity.ok(service.getList(from, to, customerName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaseDTO> updateLeaseRecord(@RequestBody LeaseRequest req, @PathVariable Long id){
        return ResponseEntity.ok(service.updateLease(req, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDeleteLease(id);
        return ResponseEntity.noContent().build();
    }
}
