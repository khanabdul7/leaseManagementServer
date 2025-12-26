package com.example.lease_management.mapper;

import com.example.lease_management.dto.Item.ItemDTO;
import com.example.lease_management.dto.Item.ItemRequest;
import com.example.lease_management.entity.Item;

public class ItemMapper {

    public static ItemDTO toDTO(Item itemEntity){
        return new ItemDTO(itemEntity.getId(), itemEntity.getName(), itemEntity.getPricePerDay());
    }

    public static Item toEntity(ItemRequest itemReq){
        return Item.builder()
                .name(itemReq.name().trim())
                .pricePerDay(itemReq.pricePerDay())
                .deleted(false)
                .build();
    }

    public static void update(Item e, ItemRequest req){
        e.setName(req.name().trim());
        e.setPricePerDay(req.pricePerDay());
    }
}
