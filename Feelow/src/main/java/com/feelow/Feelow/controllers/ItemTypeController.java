package com.feelow.Feelow.controllers;

import com.feelow.Feelow.dto.ItemTypeDto;
import com.feelow.Feelow.entity.ItemType;
import com.feelow.Feelow.service.ItemService;
import com.feelow.Feelow.service.ItemTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@RestController
public class ItemTypeController {
    @Autowired
    private ItemTypeService itemTypeService;

    @PostMapping("/itemType/upload")
    public ResponseEntity<ItemType> iupload(@RequestBody ItemTypeDto itemTypeDto){
        ItemType itemType = itemTypeService.iupload(itemTypeDto);
        return (itemType != null)?
                ResponseEntity.status(HttpStatus.OK).body(itemType):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/itemType/{itemTypeId}") // 특정 아이템 타입 매핑
    public ResponseEntity<ItemType> getItemType(@PathVariable Long itemTypeId){
        ItemType itemType = itemTypeService.getItemType(itemTypeId);
        return (itemType != null)?
                ResponseEntity.status(HttpStatus.OK).body(itemType):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/itemType/all")
    public ResponseEntity<List<ItemType>> getAllItemType(){
        List<ItemType> list = itemTypeService.getAllItemType();
        return (list != null)?
                ResponseEntity.status(HttpStatus.OK).body(list):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
