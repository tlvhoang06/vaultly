package com.hoang.vaultly.modules.fund.dto.response;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.fund.enums.FundStatus;
import java.time.Instant;
import java.util.UUID;

public record FundCreationResponse(
        UUID fundId,
        String name,
        String description,
        FundStatus status,
        String currency,
        UUID createdBy,
        Instant createdAt
) {
}
