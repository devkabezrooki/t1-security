package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private final TokenService tokenService;

    public JwtTokenProvider(@Nonnull TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String generateToken(SystemUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getRoles());
        claims.put("email", userDetails.getEmail());
        claims.put("username", userDetails.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime() + jwtExpiration)))
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public boolean isTokenValid(String token, SystemUserDetails userDetails) {
        Optional<Token> maybeLastSavedToken = tokenService.getLastTokenForUser(userDetails);
        if (maybeLastSavedToken.isEmpty()) {
            return false;
        } else {
            Token lastSavedToken = maybeLastSavedToken.get();
            return lastSavedToken.getToken().equals(token) && !lastSavedToken.isExpired();
        }
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).get("username", String.class);
    }

    public Date extractExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
