package com.hoang.vaultly.modules.ledger.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MemberBalanceResponse(
        UUID fundId,
        UUID memberId,
        UUID userId,
        String currency,
        BigDecimal equityBalance
) {}
