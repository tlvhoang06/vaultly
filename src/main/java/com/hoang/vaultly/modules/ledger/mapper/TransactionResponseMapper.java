package com.hoang.vaultly.modules.ledger.mapper;

import com.hoang.vaultly.modules.ledger.dto.response.LedgerEntryResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionDetailResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.entity.LedgerEntry;
import com.hoang.vaultly.modules.ledger.entity.LedgerTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionResponseMapper {
    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "fundId", source = "fund.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    LedgerTransactionResponse toLedgerTransactionResponse(LedgerTransaction ledgerTransaction);

    @Mapping(target = "transactionId", source = "id")
    @Mapping(target = "fundId", source = "fund.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "entries", expression = "java(toEntryResponses(ledgerTransaction.getLedgerEntries()))")
    LedgerTransactionDetailResponse toLedgerTransactionDetailResponse(LedgerTransaction ledgerTransaction);

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
