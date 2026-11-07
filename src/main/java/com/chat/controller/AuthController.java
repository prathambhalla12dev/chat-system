package com.chat.controller;

import com.chat.dto.ApiResponse;
import com.chat.dto.LoginRequest;
import com.chat.dto.RegisterRequest;
import com.chat.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody RegisterRequest request
    ) {
        ApiResponse<Void> response = authService.register(request);

        return ResponseEntity
                .status(response.getHttpCode())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @RequestBody LoginRequest request
    ) {
        ApiResponse<Void> response = authService.login(request);

        return ResponseEntity
                .status(response.getHttpCode())
                .body(response);
    }
}
