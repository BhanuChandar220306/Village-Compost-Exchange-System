package com.village.compost.controller;

import com.village.compost.dto.request.CreateCompostRequest;
import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.CompostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compost")
@RequiredArgsConstructor
public class CompostController {

    private final CompostService compostService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse> requestCompost(
            @Valid @RequestBody CreateCompostRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .success(true)
                        .message("Compost request created")
                        .data(compostService.createRequest(request))
                        .build());
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> myRequests() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("My Compost Requests")
                        .data(compostService.myRequests())
                        .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allRequests() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("All Compost Requests")
                        .data(compostService.allRequests())
                        .build());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approve(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Request Approved")
                        .data(compostService.approveRequest(id))
                        .build());
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> reject(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Request Rejected")
                        .data(compostService.rejectRequest(id))
                        .build());
    }
}