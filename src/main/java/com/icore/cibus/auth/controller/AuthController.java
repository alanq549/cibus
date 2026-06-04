package com.icore.cibus.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icore.cibus.auth.dto.request.LoginRequest;
import com.icore.cibus.auth.dto.request.RegisterRequest;
import com.icore.cibus.auth.dto.response.AuthResponse;
import com.icore.cibus.auth.service.AuthService;
import com.icore.cibus.shared.dto.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .exito(true)
                .mensaje("Inicio de sesion correcto")
                .datos(authService.login(request))
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .exito(true)
                .mensaje("Usuario registrado correctamente")
                .datos(authService.register(request))
                .build());
    }
}
