package com.example.RolebaseAuth.admin;

import com.example.RolebaseAuth.annotations.PermissionGuard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role_base/deleteUser")
public class AdminController {

    private final AdminService adminService;

    @PermissionGuard(value = {"DELETE_USER", "UPDATE_USER"})
    @DeleteMapping(value = "/deleteUser")
    ResponseEntity<Object> delete(@RequestBody AdminRequest adminRequest){
        return ResponseEntity.ok(adminService.DeleteUser(adminRequest));

    }

}
