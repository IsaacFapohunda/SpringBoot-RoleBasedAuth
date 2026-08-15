package com.example.RolebaseAuth.authentication;

import com.example.RolebaseAuth.AuthenticationToken.AuthTokenModel;
import com.example.RolebaseAuth.AuthenticationToken.AuthTokenRequest;
import com.example.RolebaseAuth.exception.ApiExceptions;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.AuthenticationToken.TokenRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import com.example.RolebaseAuth.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor

public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public BaseServerResponse<Object> authenticate(AuthenticationRequest authenticationRequest){
        BaseServerResponse serverResponse = new BaseServerResponse();
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
               .orElseThrow(()-> new ApiExceptions("Invalid username or password", 409));
            if(!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())){
                throw new ApiExceptions("Invalid username or password", 409);
            }

            Map<String, Object> jwtToken = jwtService.generateToken(user);

        AuthTokenModel authToken = tokenRepository.findByUser(user).orElse(new AuthTokenModel());

        authToken.setUser(user);
        authToken.setAccessToken(jwtToken.get("access_token").toString());
        authToken.setRefreshToken(jwtToken.get("refresh_token").toString());
                    authToken.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        System.out.println("iniit");
          AuthTokenModel savedOtpp =  tokenRepository.save(authToken);
        System.out.println("token saved");
        System.out.println(authToken);
            serverResponse.setSuccess(true);
            serverResponse.setResponseCode("200");
            serverResponse.setResponseMessage("Successful Login");
           serverResponse.setResponseData(jwtToken);
        return serverResponse;

    }


    public BaseServerResponse<Object> refreshToken(AuthTokenRequest authTokenRequest){
        BaseServerResponse serverResponse = new BaseServerResponse();
        AuthTokenModel authenTokenModel = tokenRepository.findByrefreshToken(authTokenRequest.getRefreshToken()).orElseThrow(()->
                new RuntimeException("Refresh token not found"));

        System.out.println(authenTokenModel);

        if(authenTokenModel.getExpiryDate().isAfter(LocalDateTime.now())){

            Map<String, Object> jwtToken = jwtService.generateToken(authenTokenModel.getUser());

            authenTokenModel.setUser(authenTokenModel.getUser());
            authenTokenModel.setAccessToken(jwtToken.get("access_token").toString());
            authenTokenModel.setRefreshToken(jwtToken.get("refresh_token").toString());


            AuthTokenModel savedOtpp =  tokenRepository.save(authenTokenModel);

            System.out.println(savedOtpp);
            serverResponse.setSuccess(true);
            serverResponse.setResponseCode("200");
            serverResponse.setResponseMessage("Token refreshed");
            serverResponse.setResponseData(jwtToken);
            return serverResponse;
             }

        if(authenTokenModel.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new ApiExceptions("Refresh Token expired", 401);
        }



        return serverResponse;

    }





}
