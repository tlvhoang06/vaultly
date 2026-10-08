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

        @DecimalMax(value = "100.0", message = "Contribution ratio must not exceed 100")
        @DecimalMin(value = "0.0", message = "Contribution ratio must be greater than or equal to 0")
        BigDecimal contributionRatio,

        @NotEmpty(message = "At least one role must be assigned")
        Set<FundRole> roles
) {
}
