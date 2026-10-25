package com.hoang.vaultly.modules.ledger.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record LedgerTransactionRequest(
        @NotNull(message = "Transaction amount is required")
        BigDecimal amount,

        String description,

        @NotNull(message = "Idempotency key is required")
        UUID idempotencyKey
) {
}
