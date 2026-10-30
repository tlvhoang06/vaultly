package com.hoang.vaultly.modules.auth.controller;

import com.hoang.vaultly.common.dto.ApiResponse;
import com.hoang.vaultly.modules.auth.dto.request.*;
import com.hoang.vaultly.modules.auth.dto.response.AuthResponse;
import com.hoang.vaultly.modules.auth.dto.response.ChangePasswordResponse;
import com.hoang.vaultly.modules.auth.dto.response.IntrospectResponse;
import com.hoang.vaultly.modules.auth.dto.response.RegisterResponse;
import com.hoang.vaultly.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request){
        return ApiResponse.success(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponse.success(authService.register(request));
    }

    @PostMapping("/change-password")
    public ApiResponse<ChangePasswordResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        return ApiResponse.success(authService.changePassword(request));
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request){
        return ApiResponse.success(authService.introspect(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request){
        return ApiResponse.success(authService.refreshToken(request));
    }
}
