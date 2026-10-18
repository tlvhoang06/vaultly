package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.ledger.enums.EntryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    LedgerTransaction transaction;

    @Enumerated(EnumType.STRING)
    EntryType entryType;

    @ManyToOne
    @JoinColumn(name = "ledger_account_id")
    LedgerAccount ledgerAccount;

    BigDecimal amount;
}
