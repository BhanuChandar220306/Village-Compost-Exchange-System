package com.village.compost.dto.response;

import com.village.compost.enums.UserRole;
import com.village.compost.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String fullName;
    private String email;
    private UserRole role;
    private UserStatus status;
    private String token;
    private String message;
}