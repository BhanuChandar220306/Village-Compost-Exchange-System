package com.village.compost.controller;

import com.village.compost.dto.request.AddInventoryRequest;
import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addInventory(
            @RequestBody AddInventoryRequest request) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Inventory Added")
                        .data(inventoryService.addInventory(request))
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allInventory() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Inventory List")
                        .data(inventoryService.getAllInventory())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateInventory(
            @PathVariable Long id,
            @RequestBody AddInventoryRequest request) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Inventory Updated")
                        .data(inventoryService.updateInventory(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteInventory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Inventory Deleted")
                        .data(inventoryService.deleteInventory(id))
                        .build()
        );
    }
}