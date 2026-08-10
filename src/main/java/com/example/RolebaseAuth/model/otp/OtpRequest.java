package com.example.RolebaseAuth.model.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {

    private String email;
    private int otp;
}
