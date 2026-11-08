package com.village.compost.dto.response;

import com.village.compost.enums.UserRole;
import com.village.compost.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private UserRole role;

    private String village;

    private String address;

    private UserStatus status;

}