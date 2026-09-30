package com.hoang.vaultly.modules.fund.dto.request;

import com.hoang.vaultly.modules.fund.enums.FundRole;
import jakarta.validation.constraints.NotNull;

public record RemoveRoleRequest(
        @NotNull(message = "Role is required")
        FundRole role
) {
}
