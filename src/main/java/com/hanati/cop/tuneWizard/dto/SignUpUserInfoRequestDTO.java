package com.hanati.cop.tuneWizard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignUpUserInfoRequestDTO {

    String userId;
    String password;
    String userName;
    String loginStatus;

    @Builder
    SignUpUserInfoRequestDTO(
            String userId,
            String password,
            String userName,
            String loginStatus
    ) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.loginStatus = loginStatus;
    }
}
