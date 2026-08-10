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


    @PostMapping("/confirmOtp")
    ResponseEntity<Object> confirmRegistrationOtp(@RequestBody OtpRequest otpRequest) {
        return ResponseEntity.ok().body(otpService.confirmOtp(otpRequest));
    }

    @GetMapping("/resendOtp")
    ResponseEntity<Object> resendRegistrationOtp(@RequestBody OtpRequest otpRequest) {
        return ResponseEntity.ok().body(otpService.resendOtp(otpRequest));
    }


    @PostMapping("/sendForgetPasswordOtp")
    ResponseEntity<Object> getForgetPasswordOtp(@RequestBody ForgetPasswordResetRequest forgetPasswordResetRequest){
        System.out.println("send forget password otp works");
        return ResponseEntity.ok().body(otpService.SendForgetPasswordOtp(forgetPasswordResetRequest));
    }

    @PostMapping("/forgetpasswordReset")
    ResponseEntity<Object> forgetPasswordReset(@RequestBody ForgetPasswordResetRequest forgetPasswordResetRequest){
        return ResponseEntity.ok().body(otpService.ForgetPasswordReset(forgetPasswordResetRequest));
    }
}
