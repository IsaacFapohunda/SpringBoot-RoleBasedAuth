package com.example.RolebaseAuth.model;

import com.example.RolebaseAuth.profilePicture.ProfilePictureModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.beans.ConstructorProperties;

@Data
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String firstName;
    private String profilePicture;


}
