package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.TokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private final TokenService tokenService;

    @Autowired
    public JwtTokenProvider(@Nonnull TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String generateToken(SystemUserDetails userDetails) {
        Date issuedDate = new Date();
        Date expirationDate = new Date(issuedDate.getTime() + jwtExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("roles", userDetails.getRoles());
        claims.put("email", userDetails.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public boolean isTokenValid(String token, SystemUserDetails userDetails) {
        Optional<TokenEntity> maybeLastSavedToken = tokenService.getLastTokenForUser(userDetails);
        if (maybeLastSavedToken.isEmpty()) {
            return false;
        } else {
            TokenEntity lastSavedTokenEntity = maybeLastSavedToken.get();
            return lastSavedTokenEntity.getToken().equals(token) && !lastSavedTokenEntity.isExpired();
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
