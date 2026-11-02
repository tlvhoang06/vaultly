package com.hoang.vaultly.modules.user.controller;

import com.hoang.vaultly.common.dto.ApiResponse;
import com.hoang.vaultly.modules.user.dto.request.UpdateProfileRequest;
import com.hoang.vaultly.modules.user.dto.response.UserResponse;
import com.hoang.vaultly.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyProfile(){
        return ApiResponse.success(userService.getMyProfile());
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request){
        return ApiResponse.success(userService.updateProfile(request));
    }

    @GetMapping("/search")
    public ApiResponse<Page<UserResponse>> searchUsers(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "username") Pageable pageable
    ) {
        return ApiResponse.success(userService.searchUsers(keyword, pageable));
    }
}
