package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.TokenEntity;
import com.example.t1security.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    public void testSaveToken() {
        TokenEntity tokenEntity = new TokenEntity();

        tokenService.saveToken(tokenEntity);

        verify(tokenRepository, times(1)).save(tokenEntity);
    }

    @Test
    public void testGetLastTokenForUser() {
        SystemUserDetails user = new SystemUserDetails();
        TokenEntity tokenEntity = new TokenEntity();
        when(tokenRepository.findTopByUserDetailsOrderByExpirationDateDesc(user)).thenReturn(Optional.of(tokenEntity));

        Optional<TokenEntity> result = tokenService.getLastTokenForUser(user);

        verify(tokenRepository, times(1)).findTopByUserDetailsOrderByExpirationDateDesc(user);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(tokenEntity);
    }
}
