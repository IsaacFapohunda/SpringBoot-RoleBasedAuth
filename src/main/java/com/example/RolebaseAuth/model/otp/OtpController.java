package com.example.RolebaseAuth.model.otp;


import com.example.RolebaseAuth.model.UserService;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role_base/auth")

public class OtpController {
    private final OtpService otpService;
    private final UserService userService;



    @GetMapping("/confirmOtp")
    ResponseEntity<Object> confirmRegistrationOtp(@RequestParam int otp, OtpRequest otpRequest) {
        return ResponseEntity.ok().body(otpService.confirmOtp(otp, otpRequest));
    }

    @GetMapping("/sendForgetPasswordOtp")
    ResponseEntity<Object> getForgetPasswordOtp(@RequestParam String email){
        System.out.println("send forget password otp works");
        return ResponseEntity.ok().body(otpService.SendForgetPasswordOtp(email));
    }

    @PostMapping("/forgetpasswordReset")
    ResponseEntity<Object> forgetPasswordReset(@RequestParam int otp, @RequestBody ForgetPasswordResetRequest forgetPasswordResetRequest){
        return ResponseEntity.ok().body(otpService.ForgetPasswordReset(otp, forgetPasswordResetRequest));
    }
}
