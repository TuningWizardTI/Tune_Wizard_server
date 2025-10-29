package com.hanati.cop.tuneWizard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginRequestDTO {
    String userId;
    String password;

    @Builder
    LoginRequestDTO(
            String userId,
            String password
    ) {
        this.userId = userId;
        this.password = password;
    }
}
