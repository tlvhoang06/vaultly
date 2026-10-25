package com.hoang.vaultly.modules.ledger.mapper;

import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.entity.LedgerEntry;
import com.hoang.vaultly.modules.ledger.entity.LedgerTransaction;
import com.hoang.vaultly.modules.ledger.enums.EntryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TransactionResponseMapper {
    @Mapping(target = "ledgerEntries", ignore = true)
    @Mapping(target = "fundId", source = "fund.id")
    @Mapping(target = "idempotencyKey", ignore = true)
    @Mapping(target = "amount", expression = "java(calculateTransactionAmount(ledgerTransaction))")
    LedgerTransactionResponse toLedgerTransactionResponse(LedgerTransaction ledgerTransaction);

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
}
