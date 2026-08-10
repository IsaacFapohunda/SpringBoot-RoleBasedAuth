package com.example.RolebaseAuth.model.otp;


import com.example.RolebaseAuth.exception.ApiExceptions;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserService;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@Data
@Service
@AllArgsConstructor
public class OtpService {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public int generateOtp(){
        SecureRandom random = new SecureRandom();
        int otp = 1000 + random.nextInt((9000));
        String otpStr = String.format("%0" + 4 + "d", otp);
        System.out.println(otpStr.length() + "length of otp");
        return otp;
    }

    public void saveOtp(OtpModel otp){

      otpRepository.save(otp);
        System.out.println("otp saved");
        System.out.println(otp);
    }


    public Optional<OtpModel> getToken(OtpRequest otpRequest){
        return otpRepository.findByUser_EmailAndOtp(otpRequest.getEmail(), otpRequest.getOtp());
    }

    @Transactional
    public BaseServerResponse<Object> confirmOtp(OtpRequest otpRequest){
        BaseServerResponse serverResponse = new BaseServerResponse();

        OtpModel savedOtp = otpRepository.findByUser_EmailAndOtp(otpRequest.getEmail(), otpRequest.getOtp()).orElseThrow(() ->
                 new ApiExceptions("invalid otp", 400));

        int updateTable = otpRepository.confirmedTokenIfValid(otpRequest.getOtp(), LocalDateTime.now(), LocalDateTime.now());
        System.out.println("update table");

            ///This
            if(savedOtp.getConfirmedAt() != null){
                throw new ApiExceptions("Otp already used", 409);
            }

            ///This
            if(savedOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new ApiExceptions("otp expired", 401);
            }

            userService.enableAppUser(savedOtp.getUser().getEmail());
            serverResponse.setSuccess(true);
            serverResponse.setResponseMessage("Otp confirmed");
            return serverResponse;

    }


    @Transactional
    public BaseServerResponse<Object> resendOtp(OtpRequest otpRequest){
        BaseServerResponse serverResponse = new BaseServerResponse();

        User user = userRepository.findByEmail(otpRequest.getEmail()).orElseThrow(()->
                new ApiExceptions("User not found", 400));
        int otp = generateOtp();

        OtpModel otpModel = new OtpModel(
                otp,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        OtpResponse otpResponse =
                new OtpResponse(
                otpModel.getOtp()
        );

        saveOtp(otpModel);
        serverResponse.setSuccess(true);
        serverResponse.setResponseCode("200");
        serverResponse.setResponseMessage("otp resent");
        serverResponse.setResponseData(otpResponse);

        return serverResponse;

    }




    public BaseServerResponse<Object> SendForgetPasswordOtp(ForgetPasswordResetRequest forgetPasswordResetRequest) {
        BaseServerResponse response = new BaseServerResponse<>();

        User user = userRepository.findByEmail(forgetPasswordResetRequest.getEmail()).orElseThrow(()->
                new ApiExceptions("User not found", 400));
            int otp = generateOtp();
            OtpModel forgetpasswordOtp = new OtpModel(
                    otp,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user);
            saveOtp(forgetpasswordOtp);

            response.setResponseCode("200");
            response.setResponseMessage("Your reset password Otp has been sent to your email");
            response.setSuccess(true);
            response.setResponseData(forgetpasswordOtp.getOtp());

        return response;
    }

    public BaseServerResponse ForgetPasswordReset(ForgetPasswordResetRequest forgetPasswordResetRequest) {
        BaseServerResponse response = new BaseServerResponse<>();
        BaseServerResponse Send = SendForgetPasswordOtp(forgetPasswordResetRequest);
        OtpModel otpHolder = otpRepository.findByUser_EmailAndOtp
                (forgetPasswordResetRequest.getEmail(),
                        forgetPasswordResetRequest.getOtp()).orElseThrow(() -> new ApiExceptions("User not found", 400));


        Optional<User> user = userRepository.findByEmail(forgetPasswordResetRequest.getEmail() );
        //find the user with the otp
        //if confirmed
        if(Objects.isNull(user)){
            response.setResponseCode("0");
            response.setResponseMessage("Unknown user");
            response.setSuccess(false);
        }
       user.get().setPassword(passwordEncoder.encode(forgetPasswordResetRequest.getNewPassword()));

        userRepository.save(user.get());
        response.setResponseCode("200");
        response.setResponseMessage("Your password is now updated");
        response.setSuccess(true);
        return response;
    }

}
