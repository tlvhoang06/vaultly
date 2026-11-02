package com.hoang.vaultly.modules.ledger.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record FundBalanceResponse(
        UUID fundId,
        String currency,
        BigDecimal cashBalance,
        BigDecimal totalExpense,
        BigDecimal myEquity
) {}