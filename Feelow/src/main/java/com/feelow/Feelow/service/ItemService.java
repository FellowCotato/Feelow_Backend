package com.feelow.Feelow.service;


import com.feelow.Feelow.domain.entity.Item;
import com.feelow.Feelow.domain.dto.ItemDto;
import com.feelow.Feelow.repository.ItemRepository;
import com.feelow.Feelow.repository.ItemTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final ItemTypeRepository itemTypeRepository;


    public void uploadItem(ItemDto itemDto){
        Item item = convertDtoToEntity(itemDto);
        itemRepository.save(item);
    }

    public List<ItemDto> getItemListDto() {
        List<Item> itemList = itemRepository.findAll();
        return itemList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

    }

    public List<ItemDto> getItemListByType(Long typeId){
        List<Item> itemList = itemRepository.findByTypeId(typeId);
        return itemList.stream().map(this::convertEntityToDto).collect(Collectors.toList());
        //return itemList.stream()
        //        .map(this::convertEntityToDto)
        //        .collect(Collectors.toList());

    }

    private Item convertDtoToEntity(ItemDto itemNDto){
        return Item.builder()
                .name(itemNDto.getName())
                .typeId(itemNDto.getTypeId())
                .intro(itemNDto.getIntro())
                .price(itemNDto.getPrice())
                .itemId(itemNDto.getItemId())
                .created_at(itemNDto.getCreated_at())
                .updated_at(itemNDto.getUpdated_at())
                .build();
    }


    private ItemDto convertEntityToDto(Item item){
        return ItemDto.builder()
                .itemId(item.getItemId())
                .name(item.getName())
                .typeId(item.getTypeId())
                .intro(item.getIntro())
                .price(item.getPrice())
                .itemId(item.getItemId())
                .created_at(item.getCreated_at())
                .updated_at(item.getUpdated_at())
                .build();
    }


}
