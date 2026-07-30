package com.example.RolebaseAuth.registration;

import com.example.RolebaseAuth.annotations.PermissionGuard;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Data
@Builder
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/role_base/auth")
public class RegistrationController {
    private final RegistrationService registrationService;


    @PermissionGuard(value = {"CREATE_USER"})
    @PostMapping(value = "/create")
    public ResponseEntity<Object> createUser(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request) throws IOException {
        log.info("Inside controller");
        BaseServerResponse<Object> response = registrationService.createUser(registrationRequest);
        if(!response.isSuccess()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
           // or make BaseServerResponse carry the HTTP status itself
            //return ResponseEntity.status(response.getHttpStatus()).body(response);
        }
        return  ResponseEntity.ok(response);

    }
}
