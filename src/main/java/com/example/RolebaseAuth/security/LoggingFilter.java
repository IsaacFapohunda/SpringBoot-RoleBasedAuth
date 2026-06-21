package com.example.RolebaseAuth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

@Slf4j
@Component
@Order(1)
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Starting api request");
        log.info("URL: {}", request.getRequestURI());
        log.info("ClientIP: {}", request.getRemoteAddr());
        Enumeration<String> enumeration = request.getHeaderNames();
        Iterator<String> iterator = enumeration.asIterator();
        while (iterator.hasNext()){
            String headerKey = iterator.next();
            System.out.println(headerKey + " : " + request.getHeader(headerKey));
        }

        filterChain.doFilter(request, response);
    }
}
