package com.hoang.vaultly.modules.user.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.user.dto.request.UpdateProfileRequest;
import com.hoang.vaultly.modules.user.dto.response.UserResponse;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.mapper.UserMapper;
import com.hoang.vaultly.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getCurrentUser() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponse getMyProfile() {
        User user = getCurrentUser();
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        user.setDisplayName(request.displayName());
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
