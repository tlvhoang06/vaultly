package com.hoang.vaultly.modules.user.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String displayName
) {
}
