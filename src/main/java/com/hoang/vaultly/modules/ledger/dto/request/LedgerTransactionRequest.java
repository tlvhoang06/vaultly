package com.hoang.vaultly.modules.ledger.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record LedgerTransactionRequest(
        @NotNull(message = "Transaction amount is required")
        @Positive(message = "Amount must be greater than 0")
        BigDecimal amount,

        String description,

        @NotNull(message = "Idempotency key is required")
        UUID idempotencyKey
) {
}
