package com.example.lease_management.service;

import com.example.lease_management.Repos.ItemRepo;
import com.example.lease_management.dto.Item.ItemDTO;
import com.example.lease_management.dto.Item.ItemRequest;
import com.example.lease_management.entity.Item;
import com.example.lease_management.mapper.ItemMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ItemService {

    private final ItemRepo repo;

    public ItemService(ItemRepo repo){
        this.repo = repo;
    }

    public ItemDTO create(ItemRequest req){
        if(repo.existsByNameIgnoreCase(req.name())){
            throw new DuplicateNameException("Item with name already exist!");
        }
        Item Saved = repo.save(ItemMapper.toEntity(req));

        return ItemMapper.toDTO(Saved);
    }

    public Page<ItemDTO> getAll(Pageable pageable){
        return repo.findAllActive
                (pageable).map(ItemMapper::toDTO);
    }

    public Page<ItemDTO> list(String name, Pageable pageable){
        Page<Item> page;

        if (name == null || name.isBlank()) {
            page = repo.findAllActive(pageable);
        } else {
            page = repo.searchActive(name.trim(), pageable);
        }

        return page.map(ItemMapper::toDTO);
    }

    public ItemDTO update(Long id, ItemRequest req){
        Item existedItem = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Item doesn't exist !"));

        if(!existedItem.getName().equalsIgnoreCase(req.name()) && repo.existsByNameIgnoreCase(req.name())){
            throw new DuplicateNameException("Item with same name already exists !");
        }
        ItemMapper.update(existedItem, req);
        return ItemMapper.toDTO(repo.save(existedItem));
    }

    @Transactional
    public void delete(Long id){
//        if(!repo.existsById(id)){
//            throw new EntityNotFoundException("Item not found !");
//        }
//        try{
//            repo.deleteById(id);
//        }catch (DataIntegrityViolationException dive){
//            // need to soft-delete here.
//            throw dive;
//        }

        //softDelete Code Below
        Item item = repo.findById(id).orElseThrow();
        item.setDeleted(true);
        repo.save(item);
    }


    //custom runtime exception
    public static class DuplicateNameException extends RuntimeException{
        public DuplicateNameException(String msg){
            super(msg);
        }
    }
}
