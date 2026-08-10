package com.example.RolebaseAuth.model.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordResetRequest {
    private String email;
    private Integer otp;
    private String newPassword;
}
