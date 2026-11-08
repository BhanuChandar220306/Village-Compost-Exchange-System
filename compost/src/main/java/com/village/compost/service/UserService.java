package com.village.compost.service;

import com.village.compost.dto.request.LoginRequest;
import com.village.compost.dto.request.RegisterRequest;
import com.village.compost.dto.response.LoginResponse;
import com.village.compost.dto.response.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}