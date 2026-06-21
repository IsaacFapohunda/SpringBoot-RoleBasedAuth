//package com.example.RolebaseAuth.security;
//
//import com.example.RolebaseAuth.model.User;
//import com.example.RolebaseAuth.model.UserService;
//import com.example.RolebaseAuth.payloads.BaseServerResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//@Order(3)
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final UserService userService;
//    private final JwtService jwtService;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain)
//            throws ServletException, IOException {
//
//
//        final String authHeader = request.getHeader("Authorization");
//        final String jwtToken;
//        final String email;
//        BaseServerResponse serverResponse = new BaseServerResponse<>();
//        final String url = request.getRequestURI();
//
//
//        if(url.startsWith("/api/v1/role_base/auth")){
//            filterChain.doFilter(request, response);
//        }
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")){
//            log.info("Authorization header is missing or invalid");
//            return;
//        }
//
//        jwtToken = authHeader.substring(7);
//        email =  jwtService.extractUserName(jwtToken);
//        request.setAttribute("jwtToken", jwtToken);
//        log.info(jwtToken + "in the jwtFilter");
//
//
//        if(Objects.isNull(email)){
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//            //log.info("The user account dont exist anymore so tokin is invalid");
//
//        }
//
//        if(email != null){
//         User LoggedInUserEmail = this.userService.loadUserByUsername(email);
//
//            if(jwtService.isTokenValid(jwtToken, LoggedInUserEmail)){
//                log.info("Correct <<<<<<<<<<<<<<<<<<<");
//            }
//
//            filterChain.doFilter(request, response);
//
//        }
//
//
////            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            serverResponse.setResponseMessage("Invalid JWT Token");
////            String jsonResponse = objectMapper.writeValueAsString(serverResponse.getResponseCode());
////            response.getWriter().write(jsonResponse);
//
//
//    }
//
//
//
//}
//
//
