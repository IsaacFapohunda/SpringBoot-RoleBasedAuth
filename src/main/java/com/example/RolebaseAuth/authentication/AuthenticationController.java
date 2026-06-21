package com.example.RolebaseAuth.authentication;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Data
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/role_base/auth")
public class AuthenticationController {

    private AuthenticationRequest authenticationRequest;
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok().body(authenticationService.authenticate(authenticationRequest));
    }
}
