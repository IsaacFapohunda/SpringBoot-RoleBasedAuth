package com.example.RolebaseAuth.security;


import com.example.RolebaseAuth.annotations.PermissionGuard;
import com.example.RolebaseAuth.model.Permission;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserRole;
import com.example.RolebaseAuth.model.UserService;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserService userService;

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("JWT interceptor here.....................");

        BaseServerResponse serverResponse = new BaseServerResponse();
        String url = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String email;

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        if (url.startsWith("/api/v1/role_base/auth/")) {
            return true;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            serverResponse.setSuccess(false);
            serverResponse.setResponseCode("01");
            serverResponse.setResponseMessage("Authorization header is missing or invalidkey");
            String jsonResponse = objectMapper.writeValueAsString(serverResponse);
            response.getWriter().write(jsonResponse);
            return false;
        }

        jwtToken = authHeader.substring(7);

        //perhaps i will have to have it extract just the access_token from the map and use it to extract username here
        email = jwtService.extractUserName(jwtToken);

        try{
            if (Objects.isNull(email)) {
                log.info("£££££££££££££33");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                serverResponse.setSuccess(false);
                serverResponse.setResponseCode("01");
                serverResponse.setResponseMessage("Tokin is invalid");
                String jsonResponse = objectMapper.writeValueAsString(serverResponse);
                response.getWriter().write(jsonResponse);
                return false;
            }

            User LoggedInUserEmail = this.userService.loadUserByUsername(email);
            boolean isTokenValid = jwtService.isTokenValid(jwtToken, LoggedInUserEmail);
            boolean tokenExpirationStatus = jwtService.isTokenExpired(jwtToken);

            if (isTokenValid == true) {
                request.setAttribute("jwtToken", jwtToken);
                request.setAttribute("email", email);
                log.info(jwtToken + "at the jwtInterceptor");
                return true;
            }
            if(tokenExpirationStatus == true){
                log.info("_______________________");
                serverResponse.setSuccess(false);
                serverResponse.setResponseCode("01");
                serverResponse.setResponseMessage("Token has expired");
                return false;
            }
            else{

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                serverResponse.setSuccess(false);
                serverResponse.setResponseCode("01");
                serverResponse.setResponseMessage("Tokin is invalidd");
                String jsonResponse = objectMapper.writeValueAsString(serverResponse);
                response.getWriter().write(jsonResponse);
                return false;
            }
        }catch(Exception e){
            log.info(e.getMessage() + "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQqqq");
            return false;
        }






    }
}
