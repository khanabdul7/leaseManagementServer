package com.example.lease_management.Controller;


import com.example.lease_management.dto.Item.ItemDTO;
import com.example.lease_management.dto.Item.ItemRequest;
import com.example.lease_management.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service){
        this.service = service;
    }

    @GetMapping("/{name}")
    public Page<ItemDTO> list(@RequestParam(value = "name") String name,
                              @PageableDefault(size = 10, sort = "name") Pageable pageable
    ){
        return service.list(name, pageable);
    }

    @GetMapping
    public Page<ItemDTO> getAll(@PageableDefault(size = 10, sort = "name") Pageable pageable)
    {
        return service.getAll(pageable);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> create(@RequestBody @Valid ItemRequest req){
        ItemDTO itemDTO = service.create(req);
        return ResponseEntity.created(URI.create("api/items/"+itemDTO.id()))
                .body(itemDTO);
    }

    @PutMapping("/{id}")
    public ItemDTO update(@PathVariable Long id, @RequestBody @Valid ItemRequest req){
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
