package com.example.RolebaseAuth.model.Dp;

import com.example.RolebaseAuth.profilePicture.ProfilePictureModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DpRequest {
    private String userId;
    private MultipartFile dp;
}
