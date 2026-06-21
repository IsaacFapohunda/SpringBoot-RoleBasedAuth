package com.example.RolebaseAuth.model.otp;


import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserService;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.OtpRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Data
@Service
@AllArgsConstructor
public class OtpService {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;


    public int generateOtp(){
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt((int) Math.pow(10, 4));
        String otpStr = String.format("%0" + 4 + "d", otp);
        System.out.println(otpStr.length() + "length of otp");
        return otp;
    }
    public void saveOtp(OtpModel otp){

      otpRepository.save(otp);
        System.out.println("otp saved");
        System.out.println(otp);
    }

    public Optional<OtpModel> getToken(int otp){
        System.out.println(otpRepository.findByOtp(otp) + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        return otpRepository.findByOtp(otp);
    }

    public int setConfirmedAt(int otp){
        return otpRepository.updateConfirmedAt(otp, LocalDateTime.now());
    }

    @Transactional
    public String confirmOtp(int otp){

    OtpModel otpModel = getToken(otp).orElseThrow(() -> new IllegalStateException("Token not found"));
        if(otpModel.getConfirmedAt() != null){
            throw new IllegalStateException("Email already taken");
        }
        LocalDateTime expiredAt = otpModel.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("otp expired");
        }
        setConfirmedAt(otp);
        userService.enableAppUser(otpModel.getUser().getEmail());
        return "Confirmed";
    }


    public BaseServerResponse SendForgetPasswordOtp(String email) {
        BaseServerResponse response = new BaseServerResponse<>();
        int otp = generateOtp();
        Optional<User>  user = userRepository.findByEmail(email);
        if(user != null) {
            OtpModel forgetpasswordOtp = new OtpModel(
                    otp,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user.get());
            saveOtp(forgetpasswordOtp);

            response.setResponseCode("200");
            response.setResponseMessage("Your reset password Otp has been sent to your email");
            response.setSuccess(true);
            response.setResponseData(forgetpasswordOtp.getOtp());

            return response;
        }
        else{
            response.setResponseCode("200");
            response.setResponseMessage("Your email is not associated with this account");
            response.setSuccess(true);
            return response;

        }
    }



    public BaseServerResponse ForgetPasswordReset(int otp,  ForgetPasswordResetRequest forgetPasswordResetRequest) {
        BaseServerResponse response = new BaseServerResponse<>();
        Optional<OtpModel> otpHolder = otpRepository.findByOtp(otp);
       String otpHolderEmail = otpHolder.get().getUser().getEmail();
       Optional<User> user = userRepository.findByEmail(otpHolderEmail);
       if(Objects.isNull(user)){
           response.setResponseCode("0");
           response.setResponseMessage("Unknown user");
           response.setSuccess(false);
       }
       if(!otpHolder.isPresent()){
           response.setResponseCode("0");
           response.setResponseMessage("Wrong OTP");
           response.setSuccess(false);

       }
   user.get().setPassword(forgetPasswordResetRequest.getNewPassword());
       userRepository.save(user.get());
        response.setResponseCode("200");
        response.setResponseMessage("Your password is now updated");
        response.setSuccess(true);
        return response;
    }


}
