package com.feelow.Feelow.controller;

import com.feelow.Feelow.domain.dto.ItemDto;
import com.feelow.Feelow.repository.ItemTypeRepository;
import com.feelow.Feelow.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private final ItemService itemService;

    @Autowired
    private final ItemTypeRepository itemTypeRepository;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadItem(@RequestBody final ItemDto itemDto){
        itemService.uploadItem(itemDto);
        return ResponseEntity.status(HttpStatus.OK).body("아이템 업로드 완료");
    }

    @GetMapping("/list")
    public ResponseEntity<List<ItemDto>> getItemList(){
        List<ItemDto> itemDtoList = itemService.getItemListDto();
        return new ResponseEntity<>(itemDtoList, HttpStatus.OK);
    }

    @GetMapping("/list/{typeId}")
    public ResponseEntity<List<ItemDto>> getItemListByType(@PathVariable Long typeId){
        List<ItemDto> ItemDtoList = itemService.getItemListByType(typeId);
        return new ResponseEntity<>(ItemDtoList, HttpStatus.OK);
    }
    


}
