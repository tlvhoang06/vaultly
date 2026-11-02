package com.hoang.vaultly.modules.fund.dto.response;

import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.fund.enums.FundStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record FundResponse(
        UUID id,
        String name,
        String description,
        FundStatus status,
        String currency,
        UUID createdBy,
        Instant createdAt,
        Set<FundRole> myRoles
) {}
