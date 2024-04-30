package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.Token;
import com.example.t1security.repository.TokenRepository;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(@Nonnull TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Transactional
    public Optional<Token> getLastTokenForUser(SystemUserDetails user) {
        return tokenRepository.findTopByUserDetailsOrderByExpirationDateDesc(user);
    }
}
