package com.village.compost.controller;

import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> myDashboard() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Dashboard data")
                        .data(dashboardService.getMyDashboard())
                        .build()
        );
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse> adminDashboard() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Admin dashboard data")
                        .data(dashboardService.getAdminDashboard())
                        .build()
        );
    }
}