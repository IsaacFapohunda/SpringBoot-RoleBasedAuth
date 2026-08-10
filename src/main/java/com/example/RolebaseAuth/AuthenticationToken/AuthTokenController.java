package com.example.RolebaseAuth.AuthenticationToken;

import com.example.RolebaseAuth.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role_base/auth")
public class AuthTokenController {

    final private AuthenticationService authenticationService;



    @GetMapping("/refreshToken")
    ResponseEntity<Object> refreshToken(@RequestBody AuthTokenRequest authTokenRequest){
        return ResponseEntity.ok().body(authenticationService.refreshToken(authTokenRequest));
    }
}
