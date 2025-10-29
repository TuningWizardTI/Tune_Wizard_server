package com.hanati.cop.tuneWizard.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginInfoTableDAO {
    private String userId;
    private String password;
    private String userName;
    private String loginStatus;

}
