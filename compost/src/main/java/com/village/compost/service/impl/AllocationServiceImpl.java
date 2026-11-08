
package com.village.compost.service.impl;

import com.village.compost.dto.request.AllocateRequest;
import com.village.compost.dto.response.AllocationResponse;
import com.village.compost.entity.*;
import com.village.compost.enums.AllocationStatus;
import com.village.compost.enums.RequestStatus;
import com.village.compost.enums.UserRole;
import com.village.compost.repository.*;
import com.village.compost.service.AllocationService;
import com.village.compost.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final CompostAllocationRepository allocationRepository;
    private final CompostRequestRepository compostRequestRepository;
    private final CompostInventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void notifyAdmins(String message) {
        userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.ADMIN)
                .forEach(admin ->
                        notificationService.createNotification(
                                admin.getId(), message
                        )
                );
    }

    @Override
    public AllocationResponse allocateCompost(AllocateRequest request) {

        CompostRequest compostRequest =
                compostRequestRepository.findById(
                        request.getCompostRequestId()
                ).orElseThrow(() ->
                        new RuntimeException("Compost request not found"));

        CompostInventory inventory =
                inventoryRepository.findById(request.getInventoryId())
                        .orElseThrow(() ->
                                new RuntimeException("Inventory not found"));

        if (compostRequest.getStatus() != RequestStatus.ACCEPTED) {
            throw new RuntimeException("Request is not approved");
        }

        if (inventory.getAvailableQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough compost available");
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - request.getQuantity()
        );

        inventoryRepository.save(inventory);

        CompostAllocation allocation = CompostAllocation.builder()
                .farmer(compostRequest.getFarmer())
                .compostRequest(compostRequest)
                .inventory(inventory)
                .allocatedQuantity(request.getQuantity())
                .allocatedDate(LocalDate.now())
                .status(AllocationStatus.ALLOCATED)
                .build();

        CompostAllocation saved = allocationRepository.save(allocation);

        notificationService.createNotification(
                compostRequest.getFarmer().getId(),
                "Compost has been allocated to you. Quantity: "
                        + request.getQuantity()
        );

        return map(saved);
    }

    @Override
    public List<AllocationResponse> getAllAllocations() {

        return allocationRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<AllocationResponse> getMyAllocations() {

        return allocationRepository.findByFarmer(getCurrentUser())
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public AllocationResponse completeAllocation(Long id) {

        CompostAllocation allocation =
                allocationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Allocation not found"));

        allocation.setStatus(AllocationStatus.COMPLETED);

        CompostAllocation saved =
                allocationRepository.save(allocation);

        notifyAdmins(
                "Farmer " + allocation.getFarmer().getFullName()
                        + " has completed compost allocation #"
                        + allocation.getId()
        );

        return map(saved);
    }

    private AllocationResponse map(CompostAllocation allocation) {

        return AllocationResponse.builder()
                .id(allocation.getId())
                .farmerName(allocation.getFarmer().getFullName())
                .allocatedQuantity(allocation.getAllocatedQuantity())
                .allocatedDate(allocation.getAllocatedDate())
                .status(allocation.getStatus())
                .build();
    }
}
