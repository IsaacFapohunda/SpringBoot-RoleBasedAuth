package com.example.RolebaseAuth.security;

import com.example.RolebaseAuth.annotations.PermissionGuard;
import com.example.RolebaseAuth.RoleAndPermission.Permission;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserService;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Order(2)
public class RolePermissionInterceptor implements HandlerInterceptor {
//handler interceptor has a third property called object handler which is the controler or method in the controller its handling



    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final UserService userService;
    //private User user;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Role base here.....................");

        BaseServerResponse serverResponse = new BaseServerResponse();
        //String email = request.getHeader("email");
        String url = request.getRequestURI();
       // final String jwtToken;
       final String email;

        if(url.startsWith("/api/v1/role_base/auth")){
            return true;
        }

       String jwtToken = (String) request.getAttribute("jwtToken");
        email =  jwtService.extractUserName(jwtToken);

        Optional<User> optionaluser = userRepository.findByEmail(email);

        User user = optionaluser.get();
        log.info(user.getEmail());

        if(Objects.isNull(email)){
            log.info("£££££££££££££");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            serverResponse.setSuccess(false);
            serverResponse.setResponseCode("01");
            serverResponse.setResponseMessage("Unknown user");
            String jsonResponse = objectMapper.writeValueAsString(serverResponse);
            response.getWriter().write(jsonResponse);
            return false;
        }



        log.info(jwtToken + "in the interceptor");
        log.info(email + "in the interceptor");



        response.setContentType(MediaType.APPLICATION_JSON_VALUE);


        //since i pass no email to header for it to use, then lets fimally use the jwttoken finally


        // Check for permission on the controller method if the permission guard is part of the user permissions

      //  Set<UserRole> roles = user.getRoles();
        //uncomment the above  and below 4 when you deploy
        //System.out.println(roles);
        Set<Permission> permissions = new LinkedHashSet<>();
        System.out.println(permissions);
       // roles.forEach(userRole -> permissions.addAll(userRole.getPermissions()));

        //System.out.println("All roles combined: " + roles);

        // All permission names that belongs to the user in the database
        Set<String> allPermissionNames = permissions
                .stream()
                .map(permission -> permission.getName().name())
                .collect(Collectors.toSet()); // [CREATE_USER, DELETE_USER]

        System.out.println("All permissions: " + allPermissionNames);

        // The permission that was configured in the controller
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method controllerMethod = handlerMethod.getMethod();
        PermissionGuard permissionGuard = controllerMethod.getDeclaredAnnotation(PermissionGuard.class);


        if (controllerMethod.isAnnotationPresent(PermissionGuard.class)){

            String[] configuredPermission = permissionGuard.value();
            List<String> configuredPermissionList = List.of(configuredPermission);
            System.out.println("Configured on controller: " + configuredPermissionList);

            System.out.println("The permission value on the controller method: ================>" + configuredPermissionList);

            boolean userHasPermission = allPermissionNames.containsAll(configuredPermissionList);
            if(userHasPermission){
                return true;
            }else{
                response.setStatus(HttpStatus.FORBIDDEN.value());

                serverResponse.setSuccess(false);
                serverResponse.setResponseCode("03");
                serverResponse.setResponseMessage("you don't have enough permission for this resource!");
                String jsonResponse = objectMapper.writeValueAsString(serverResponse);
                response.getWriter().write(jsonResponse);
                return false;
            }
        }
        else {
            log.info("xup");
            return true;
        }



    }

    }
