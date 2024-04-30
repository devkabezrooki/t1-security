package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.Token;
import com.example.t1security.model.UserRole;
import com.example.t1security.model.dto.SignInRequest;
import com.example.t1security.model.dto.SignUpRequest;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SecurityService {

    private final SystemUserDetailsService systemUserDetailsService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityService(@Nonnull SystemUserDetailsService systemUserDetailsService,
                           @Nonnull TokenService tokenService,
                           @Nonnull JwtTokenProvider jwtTokenProvider,
                           @Nonnull PasswordEncoder passwordEncoder,
                           @Nonnull AuthenticationManager authenticationManager) {
        this.systemUserDetailsService = systemUserDetailsService;
        this.tokenService = tokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public String register(SignUpRequest request) {

        var user = new SystemUserDetails();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(UserRole.USER));

        systemUserDetailsService.createUser(user);

        return createAndSaveNewTokenForUser(user);
    }

    @Transactional
    public String login(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = systemUserDetailsService.loadUserByUsername(request.getUsername());

        return getActualTokenForUser(user);
    }

    private String createAndSaveNewTokenForUser(SystemUserDetails user) {
        var jwt = jwtTokenProvider.generateToken(user);

        var token = new Token();
        token.setUserDetails(user);
        token.setToken(jwt);
        token.setExpirationDate(jwtTokenProvider.extractExpirationDate(jwt));

        tokenService.saveToken(token);

        return jwt;
    }

    private String getActualTokenForUser(SystemUserDetails user) {
        String jwt;
        Optional<Token> maybeLastTokenForUser = tokenService.getLastTokenForUser(user);

        if (maybeLastTokenForUser.isEmpty() || maybeLastTokenForUser.get().isExpired()) {
            jwt = createAndSaveNewTokenForUser(user);
        } else {
            jwt = maybeLastTokenForUser.get().getToken();
        }
        return jwt;
    }
}
