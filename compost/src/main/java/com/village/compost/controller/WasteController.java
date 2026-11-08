package com.village.compost.controller;
import  java.util.List;
import com.village.compost.dto.request.CreateWasteRequest;
import com.village.compost.dto.response.ApiResponse;
import com.village.compost.dto.response.WasteResponse;
import com.village.compost.service.WasteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waste")
@RequiredArgsConstructor
public class WasteController {

    private final WasteService wasteService;

    @PostMapping
    public ResponseEntity<ApiResponse> createWasteRequest(
            @Valid @RequestBody CreateWasteRequest request) {

        WasteResponse response = wasteService.createWasteRequest(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .message("Waste request created successfully")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }
    @GetMapping("/my")
    public ResponseEntity<List<WasteResponse>> myRequests() {
        return ResponseEntity.ok(wasteService.getMyRequests());
    }

    @GetMapping("/all")
    public ResponseEntity<List<WasteResponse>> allRequests() {
        return ResponseEntity.ok(wasteService.getAllRequests());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WasteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(wasteService.deleteRequest(id));
    }
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> pendingRequests() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Pending Requests")
                        .data(wasteService.getPendingRequests())
                        .build()
        );

    }
    @PutMapping("/{id}/accept")
    public ResponseEntity<ApiResponse> acceptRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Request Accepted")
                        .data(wasteService.acceptRequest(id))
                        .build()
        );

    }
    @PutMapping("/{id}/collect")
    public ResponseEntity<ApiResponse> collectRequest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Waste Collected")
                        .data(wasteService.collectRequest(id))
                        .build()
        );

    }
}