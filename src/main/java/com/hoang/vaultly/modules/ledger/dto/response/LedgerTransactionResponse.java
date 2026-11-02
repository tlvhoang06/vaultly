package com.hoang.vaultly.modules.ledger.dto.response;

import com.hoang.vaultly.modules.ledger.enums.TransactionStatus;
import com.hoang.vaultly.modules.ledger.enums.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record LedgerTransactionResponse(
        UUID transactionId,
        UUID fundId,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        BigDecimal amount,
        String description,
        UUID createdBy,
        Instant createdAt
) {
}