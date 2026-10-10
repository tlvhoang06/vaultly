package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.ledger.enums.TransactionStatus;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class LedgerTransaction {
    List<LedgerEntry> ledgerEntries;

    TransactionStatus transactionStatus;
}
