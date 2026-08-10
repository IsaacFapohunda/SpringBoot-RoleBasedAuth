package com.example.RolebaseAuth.registration;

import com.example.RolebaseAuth.RoleAndPermission.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class RegistrationResponse {
    private String id;
    private String email;
    private String firstName;

    private Set<UserRole> roles;
    private Boolean enabled;

    private Integer otp;

    public RegistrationResponse(
            String id, String email, String firstName,
            //Set<UserRole> roles,
            Boolean enabled, Integer otp) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        //this.roles = roles;
        this.enabled = enabled;
        this.otp = otp;
    }


}
