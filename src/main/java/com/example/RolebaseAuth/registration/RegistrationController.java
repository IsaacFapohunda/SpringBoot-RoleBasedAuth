package com.example.RolebaseAuth.registration;

import com.example.RolebaseAuth.annotations.PermissionGuard;
import com.example.RolebaseAuth.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Object> createUser(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request) throws JsonProcessingException {
        log.info("Inside controller");
        System.out.println("yea");
        return ResponseEntity.ok(registrationService.createUser(registrationRequest, request));
    }
}
