package com.example.t1security.controller;

import com.example.t1security.model.dto.SignInRequest;
import com.example.t1security.model.dto.SignUpRequest;
import com.example.t1security.service.SecurityService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
@Validated
public class SecurityController {

    private final SecurityService securityService;

    @Autowired
    public SecurityController(@Nonnull SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(securityService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(securityService.login(request));
    }
}
