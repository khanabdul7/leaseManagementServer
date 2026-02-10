package com.example.lease_management.service;

import com.example.lease_management.Repos.ItemRepo;
import com.example.lease_management.dto.Item.ItemDTO;
import com.example.lease_management.dto.Item.ItemRequest;
import com.example.lease_management.entity.Item;
import com.example.lease_management.entity.User;
import com.example.lease_management.mapper.ItemMapper;
import com.example.lease_management.security.CurrentUserProvider;
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
    private final CurrentUserProvider currentUserProvider;

    public ItemService(ItemRepo repo, CurrentUserProvider currentUserProvider){
        this.repo = repo;
        this.currentUserProvider = currentUserProvider;
    }

    public ItemDTO create(ItemRequest req){

        User user = currentUserProvider.get();

        if(repo.existsByNameIgnoreCaseAndUser(req.name(), user)){
            throw new DuplicateNameException("Item with name already exist!");
        }
        Item item = ItemMapper.toEntity(req);

        // 🔐 SET USER BEFORE SAVE
        item.setUser(user);
        item.setDeleted(false);

        Item saved = repo.save(item);


        return ItemMapper.toDTO(saved);
    }

    public Page<ItemDTO> getAll(Pageable pageable){
        User user = currentUserProvider.get();
        return repo.findAllActive
                (user, pageable).map(ItemMapper::toDTO);
    }

    public Page<ItemDTO> list(String name, Pageable pageable){
        User user = currentUserProvider.get();
        Page<Item> page;

        if (name == null || name.isBlank()) {
            page = repo.findAllActive(user, pageable);
        } else {
            page = repo.searchActive(user, name.trim(), pageable);
        }

        return page.map(ItemMapper::toDTO);
    }

    public ItemDTO update(Long id, ItemRequest req){
        User user = currentUserProvider.get();
        Item existedItem = repo.findByIdAndUserAndDeletedFalse(id, user).orElseThrow(()-> new EntityNotFoundException("Item doesn't exist !"));

        if(!existedItem.getName().equalsIgnoreCase(req.name()) && repo.existsByNameIgnoreCaseAndUser(req.name(), user)){
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

        User user = currentUserProvider.get();
        //softDelete Code Below
        Item item = repo.findByIdAndUserAndDeletedFalse(id, user).orElseThrow();
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
