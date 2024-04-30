package com.example.t1security.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.t1security.config.JwtAuthenticationFilter.AUTH_HEADER_NAME;

@RestController
@RequestMapping("/check")
public class AccessCheckingController {

    @GetMapping("/all")
    @SecurityRequirement(name = AUTH_HEADER_NAME)
    public ResponseEntity<String> forAll() {
        return ResponseEntity.ok("Доступно всем");
    }

    @GetMapping("/admin")
    @SecurityRequirement(name = AUTH_HEADER_NAME)
    public ResponseEntity<String> forAdmin() {
        return ResponseEntity.ok("Доступно админам");
    }
}
