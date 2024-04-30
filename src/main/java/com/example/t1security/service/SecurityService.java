package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.TokenEntity;
import com.example.t1security.model.dto.LoginRequest;
import com.example.t1security.model.dto.RegisterRequest;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final SystemUserDetailsService systemUserDetailsService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityService(@Nonnull SystemUserDetailsService systemUserDetailsService,
                           @Nonnull TokenService tokenService,
                           @Nonnull JwtTokenProvider jwtTokenProvider,
                           @Nonnull AuthenticationManager authenticationManager) {
        this.systemUserDetailsService = systemUserDetailsService;
        this.tokenService = tokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public String registerUser(RegisterRequest request) {

        SystemUserDetails user = SystemUserDetailsMapper.createUserFromRegisterRequest(request);

        systemUserDetailsService.createUser(user);

        return createAndSaveNewTokenForUser(user);
    }

    @Transactional
    public String loginUser(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        SystemUserDetails user = systemUserDetailsService.loadUserByUsername(request.getUsername());

        return getActualTokenForUser(user);
    }

    private String createAndSaveNewTokenForUser(SystemUserDetails user) {
        String jwt = jwtTokenProvider.generateToken(user);

        TokenEntity token = new TokenEntity();
        token.setUserDetails(user);
        token.setToken(jwt);
        token.setExpirationDate(jwtTokenProvider.extractExpirationDate(jwt));

        tokenService.saveToken(token);

        return jwt;
    }

    private String getActualTokenForUser(SystemUserDetails user) {
        String jwt;
        Optional<TokenEntity> maybeLastTokenForUser = tokenService.getLastTokenForUser(user);

        if (maybeLastTokenForUser.isEmpty() || maybeLastTokenForUser.get().isExpired()) {
            jwt = createAndSaveNewTokenForUser(user);
        } else {
            jwt = maybeLastTokenForUser.get().getToken();
        }
        return jwt;
    }
}
