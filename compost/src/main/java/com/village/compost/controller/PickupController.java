package com.village.compost.controller;

import com.village.compost.dto.request.CreatePickupRequest;
import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.PickupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pickup")
@RequiredArgsConstructor
public class PickupController {

    private final PickupService pickupService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestBody CreatePickupRequest request) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Pickup scheduled successfully")
                        .data(pickupService.createPickup(request))
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> all() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("All pickup schedules")
                        .data(pickupService.getAllPickups())
                        .build()
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> my() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("My pickup schedules")
                        .data(pickupService.getMyPickups())
                        .build()
        );
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse> today() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Today's pickups")
                        .data(pickupService.getTodayPickups())
                        .build()
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse> complete(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Pickup completed")
                        .data(pickupService.completePickup(id))
                        .build()
        );
    }
}