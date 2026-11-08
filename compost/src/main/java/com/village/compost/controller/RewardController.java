package com.village.compost.controller;

import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> myRewards() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Reward Points")
                        .data(rewardService.getMyRewards())
                        .build()
        );
    }
}