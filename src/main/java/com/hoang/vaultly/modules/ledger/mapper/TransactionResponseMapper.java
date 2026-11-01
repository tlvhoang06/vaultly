package com.hoang.vaultly.modules.ledger.mapper;

import com.hoang.vaultly.modules.ledger.dto.response.LedgerEntryResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionDetailResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.entity.LedgerEntry;
import com.hoang.vaultly.modules.ledger.entity.LedgerTransaction;
import com.hoang.vaultly.modules.ledger.enums.EntryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionResponseMapper {
    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "fundId", source = "fund.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "amount", expression = "java(calculateTransactionAmount(ledgerTransaction))")
    LedgerTransactionResponse toLedgerTransactionResponse(LedgerTransaction ledgerTransaction);

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "fundId", source = "fund.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "amount", expression = "java(calculateTransactionAmount(ledgerTransaction))")
    @Mapping(target = "entries", expression = "java(toEntryResponses(ledgerTransaction.getLedgerEntries()))")
    LedgerTransactionDetailResponse toLedgerTransactionDetailResponse(LedgerTransaction ledgerTransaction);

    default BigDecimal calculateTransactionAmount(LedgerTransaction ledgerTransaction) {
        if (ledgerTransaction.getLedgerEntries() == null || ledgerTransaction.getLedgerEntries().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return ledgerTransaction
                .getLedgerEntries()
                .stream()
                .filter(entry -> entry.getEntryType() == EntryType.DEBIT)
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default List<LedgerEntryResponse> toEntryResponses(List<LedgerEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return Collections.emptyList();
        }

        return entries.stream()
                .map(e -> LedgerEntryResponse.builder()
                        .entryId(e.getId())
                        .accountId(e.getLedgerAccount() != null ? e.getLedgerAccount().getId() : null)
                        .accountType(e.getLedgerAccount() != null ? e.getLedgerAccount().getAccountType() : null)
                        .entryType(e.getEntryType())
                        .amount(e.getAmount())
                        .build())
                .toList();
    }
}
