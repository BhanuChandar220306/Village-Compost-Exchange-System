package com.village.compost.controller;

import com.village.compost.dto.request.RegisterRequest;
import com.village.compost.dto.response.ApiResponse;
import com.village.compost.dto.response.UserResponse;
import com.village.compost.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.village.compost.dto.request.LoginRequest;
import com.village.compost.dto.response.LoginResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    @GetMapping("/test")
    public String test() {
        return "Controller Working";
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = userService.login(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/profile")
    public String profile() {
        return "JWT Authentication Successful";
    }
}