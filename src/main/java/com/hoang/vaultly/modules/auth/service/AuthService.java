package com.hoang.vaultly.modules.auth.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.auth.dto.request.*;
import com.hoang.vaultly.modules.auth.dto.response.AuthResponse;
import com.hoang.vaultly.modules.auth.dto.response.ChangePasswordResponse;
import com.hoang.vaultly.modules.auth.dto.response.IntrospectResponse;
import com.hoang.vaultly.modules.auth.dto.response.RegisterResponse;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.mapper.UserMapper;
import com.hoang.vaultly.modules.user.repository.UserRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),
                request.password()));

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenDuration())
                .build();
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName());
        userRepository.save(user);
        return userMapper.toRegisterResponse(user);
    }

    @Transactional
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        String currentUsername = Objects
                .requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
        User user = userRepository
                .findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check entered old password >< user password
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        // check entered new password == user password
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())){
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return new ChangePasswordResponse("Password changed successfully");
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        try {
            jwtService.validateToken(request.token(), JwtService.ACCESS_TYPE);
            return IntrospectResponse.builder().isValid(true).build();
        } catch (AppException e) {
            return IntrospectResponse.builder().isValid(false).build();
        }
    }

    public AuthResponse refreshToken(RefreshRequest request) {
        // throw AppException if refresh token is invalid/expired
        JWTClaimsSet claimsSet = jwtService.validateToken(request.refreshToken(), JwtService.REFRESH_TYPE);

        String username = claimsSet.getSubject();
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // generate new tokens
        String newRefreshToken = jwtService.generateRefreshToken(user);
        String newAccessToken = jwtService.generateAccessToken(user);

        return AuthResponse
                .builder()
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .expiresIn(jwtService.getAccessTokenDuration())
                .tokenType("Bearer")
                .build();
    }

}
