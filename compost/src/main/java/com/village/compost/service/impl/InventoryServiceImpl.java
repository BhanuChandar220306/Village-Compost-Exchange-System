package com.village.compost.service.impl;

import com.village.compost.dto.request.AddInventoryRequest;
import com.village.compost.dto.response.InventoryResponse;
import com.village.compost.entity.CompostInventory;
import com.village.compost.repository.CompostInventoryRepository;
import com.village.compost.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final CompostInventoryRepository inventoryRepository;

    @Override
    public InventoryResponse addInventory(AddInventoryRequest request) {

        CompostInventory inventory = CompostInventory.builder()
                .availableQuantity(request.getAvailableQuantity())
                .compostType(request.getCompostType())
                .location(request.getLocation())
                .build();

        return map(inventoryRepository.save(inventory));
    }

    @Override
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public InventoryResponse updateInventory(Long id,
                                             AddInventoryRequest request) {

        CompostInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setCompostType(request.getCompostType());
        inventory.setLocation(request.getLocation());

        return map(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryResponse deleteInventory(Long id) {

        CompostInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryRepository.delete(inventory);

        return map(inventory);
    }

    private InventoryResponse map(CompostInventory inventory) {

        return InventoryResponse.builder()
                .id(inventory.getId())
                .availableQuantity(inventory.getAvailableQuantity())
                .compostType(inventory.getCompostType())
                .location(inventory.getLocation())
                .build();
    }
}