package com.village.compost.controller;

import com.village.compost.dto.request.AllocateRequest;
import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/allocation")
@RequiredArgsConstructor
public class AllocationController {

    private final AllocationService allocationService;

    @PostMapping("/allocate")
    public ResponseEntity<ApiResponse> allocate(
            @RequestBody AllocateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Compost Allocated")
                        .data(allocationService.allocateCompost(request))
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> all() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("All Allocations")
                        .data(allocationService.getAllAllocations())
                        .build()
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> my() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("My Allocations")
                        .data(allocationService.getMyAllocations())
                        .build()
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> complete(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Allocation Completed")
                        .data(allocationService.completeAllocation(id))
                        .build()
        );
    }
}