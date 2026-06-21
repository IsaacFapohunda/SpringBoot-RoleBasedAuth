package com.example.RolebaseAuth.registration;

import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.otp.OtpModel;
import com.example.RolebaseAuth.model.otp.OtpService;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.OtpRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Service
@Builder
@AllArgsConstructor
@Data
public class RegistrationService {

    private final OtpService otpService;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public BaseServerResponse<Object> createUser(RegistrationRequest registrationRequest, HttpServletRequest request) throws JsonProcessingException {

        log.info("Over here~~~~~~~~~~~~~~~~~~~~~~");
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        log.info("Hey=???????????");

        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        BaseServerResponse<Object> serverResponse = new BaseServerResponse<>();
        if (userExists){
            serverResponse.setSuccess(false);
            serverResponse.setResponseCode("500");
            serverResponse.setResponseMessage("email already exists");
            String jsonResponse = objectMapper.writeValueAsString(serverResponse);
        }

        else{
            userRepository.save(user);
            System.out.println(userRepository.save(user));
            //String otp = UUID.randomUUID().toString();
            int otp = otpService.generateOtp();
            OtpModel otpModel = new OtpModel(
                    otp,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );
            otpService.saveOtp(otpModel);
            RegistrationResponse registrationResponse = new RegistrationResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getRoles(),
                    user.getEnabled(),
                    otp

            );
            serverResponse.setSuccess(true);
            serverResponse.setResponseCode("200");
            serverResponse.setResponseMessage("Registration successful");
            serverResponse.setResponseData(registrationResponse);
            String jsonResponsee = objectMapper.writeValueAsString(serverResponse);

        }

        return serverResponse;
    }
}
