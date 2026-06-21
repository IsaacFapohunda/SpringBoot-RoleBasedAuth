package com.example.RolebaseAuth.security;

import com.example.RolebaseAuth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Slf4j
@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUserName(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String jwtToken, User user){
       final String userName =  extractUserName(jwtToken);
       return (userName.equals(user.getUsername())) && !isTokenExpired(jwtToken);
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    public Map<String, Object> generateToken(
            Map<String, Object> extractClaims,
            User user
           ){
        Map<String, Object> response = new HashMap<>();
        String access_token = Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))//this is 15 minutes. 10minutes is 10 * 60secs, then milli there
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        String refresh_token = Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 24 * 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        response.put("access_token", access_token);
        response.put("refresh_token", refresh_token);

        return response;

    }

    public Map<String, Object> generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

}
