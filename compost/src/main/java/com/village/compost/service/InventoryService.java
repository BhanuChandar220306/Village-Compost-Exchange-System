package com.village.compost.service;

import com.village.compost.dto.request.AddInventoryRequest;
import com.village.compost.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse addInventory(AddInventoryRequest request);

    List<InventoryResponse> getAllInventory();

    InventoryResponse updateInventory(Long id,
                                      AddInventoryRequest request);

    InventoryResponse deleteInventory(Long id);
}