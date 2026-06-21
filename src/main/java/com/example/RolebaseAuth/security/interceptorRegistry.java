package com.example.RolebaseAuth.security;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class interceptorRegistry implements WebMvcConfigurer{

    private final RolePermissionInterceptor rolePermissionInterceptor;
   private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;


//ADDINTERCEPTORS IS A METHOD USED TO REGISTER OUR CUSTOM INTERCEPTORS IN OUR APPLICATION, ADDS THEM TO INTERCEPTORS REGISTRY, HENCE WE
//CAN HAVE MANY INTERCEPTORS. THIS PARTICULAR ONE IS A USER REGISTRATION INTERCEPTOR
    //interceptors takes us directly to the api methods hence its difference from filters

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry){

        registry.addInterceptor(jwtAuthenticationInterceptor);
        registry.addInterceptor(rolePermissionInterceptor);
    }

}
