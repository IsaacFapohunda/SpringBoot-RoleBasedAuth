package com.example.RolebaseAuth.authentication;

import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.UserRepository;
import com.example.RolebaseAuth.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor

public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    public BaseServerResponse<Object> authenticate(AuthenticationRequest authenticationRequest){
        BaseServerResponse serverResponse = new BaseServerResponse();
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
               .orElseThrow(()-> new RuntimeException("User not found"));



        try{
            if(!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())){
                serverResponse.setSuccess(false);
                serverResponse.setResponseCode("500");
                serverResponse.setResponseMessage("Invalid username or password");
                return serverResponse;
            }

            else if(!Objects.equals(authenticationRequest.getEmail(), user.getEmail())){
                serverResponse.setSuccess(false);
                serverResponse.setResponseCode("500");
                serverResponse.setResponseMessage("Bad request");
                return serverResponse;
            }
            Map<String, Object> jwtToken = jwtService.generateToken(user);

            serverResponse.setSuccess(true);
            serverResponse.setResponseCode("200");
            serverResponse.setResponseMessage("Successful Login");
           serverResponse.setResponseData(jwtToken);

        }catch(Exception e){
            System.out.println(e);
        }

        return serverResponse;

    }
}
