package com.example.RolebaseAuth.authentication;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationRequest {
    private String email;
    private String password;
}
