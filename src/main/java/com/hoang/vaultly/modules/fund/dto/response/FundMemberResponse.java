package com.hoang.vaultly.modules.fund.dto.response;

import com.hoang.vaultly.modules.fund.enums.FundRole;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record FundMemberResponse(
        UUID fundMemberId,
        UUID fundId,
        UUID userId,
        String username,
        String displayName,
        BigDecimal contributionRatio,
        Instant joinedAt,
        Set<FundRole> roles
) {}