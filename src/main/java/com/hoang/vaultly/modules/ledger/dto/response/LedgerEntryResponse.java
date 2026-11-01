package com.hoang.vaultly.modules.ledger.dto.response;

import com.hoang.vaultly.modules.ledger.enums.AccountType;
import com.hoang.vaultly.modules.ledger.enums.EntryType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record LedgerEntryResponse(
        UUID entryId,
        UUID accountId,
        AccountType accountType,
        EntryType entryType,
        BigDecimal amount
) {}
