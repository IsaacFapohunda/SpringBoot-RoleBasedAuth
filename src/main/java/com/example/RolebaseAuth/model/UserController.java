package com.example.RolebaseAuth.model;

import com.example.RolebaseAuth.annotations.PermissionGuard;
import com.example.RolebaseAuth.model.Dp.DpRequest;
import com.example.RolebaseAuth.model.password.ChangePasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role_base")
public class UserController {

    private final UserService userService;

    @PostMapping("/getUser")
    @PermissionGuard(value = {"READ_USER"})
    ResponseEntity<Object> getUser(@RequestParam String id){
        log.info("user controller baby");
        return ResponseEntity.ok().body(userService.getUserById(id));
    }


    @PostMapping("/changePassword")
    ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request){
        log.info("Change that password");
        userService.ChangePassword(request, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

}
