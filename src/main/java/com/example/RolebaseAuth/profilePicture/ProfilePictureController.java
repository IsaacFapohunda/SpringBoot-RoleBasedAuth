package com.example.RolebaseAuth.profilePicture;

import com.example.RolebaseAuth.config.S3Service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@RestController
@RequestMapping("api/v1/role_base/auth")
public class ProfilePictureController {
    private final S3Service s3Service;

    private final ProfilePictureRepo profilePictureRepo;
    private final ProfilePictureService profilePictureService;

    @PostMapping("/uploadDp")
    public ResponseEntity<Object> uploadDp(@RequestPart("file") MultipartFile file,
                                           @RequestPart("userId") String userId
    ) {
        System.out.println("upload picture controller");

        return ResponseEntity.ok().body(profilePictureService.s3SaveLinkToDb(userId, file));
    }

    @GetMapping("/getPicture")
    public ResponseEntity<Object> getDp(@RequestParam String id) {
        //NOTE
        //when tyring yo get sth from database i used id as request body didnt work but when i used as
        //requestparam it worked.
        System.out.println("upload getDp controller");
        return ResponseEntity.ok().body(profilePictureService.getFile(id));
    }


}
