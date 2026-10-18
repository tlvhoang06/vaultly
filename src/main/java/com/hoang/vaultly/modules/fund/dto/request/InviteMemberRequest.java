package com.hoang.vaultly.modules.fund.dto.request;

import com.hoang.vaultly.modules.fund.enums.FundRole;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record InviteMemberRequest(
        @NotNull(message = "User ID is required")
        UUID userId,

        @NotEmpty(message = "At least one role must be assigned")
        Set<FundRole> roles
) {
}
