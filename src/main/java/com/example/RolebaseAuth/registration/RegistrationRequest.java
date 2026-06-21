package com.example.RolebaseAuth.registration;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String firstName;
    private String email;
    private String password;

}
