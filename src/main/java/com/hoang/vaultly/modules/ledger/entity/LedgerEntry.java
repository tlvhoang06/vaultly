package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.ledger.enums.EntryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false, updatable = false)
    LedgerTransaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    EntryType entryType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ledger_account_id", nullable = false, updatable = false)
    LedgerAccount ledgerAccount;

    @Column(nullable = false, updatable = false)
    BigDecimal amount;
}
