package com.example.RolebaseAuth.AuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenRequest {
    private String refreshToken;
}
