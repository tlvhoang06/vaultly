package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.ledger.enums.TransactionStatus;
import com.hoang.vaultly.modules.ledger.enums.TransactionType;
import com.hoang.vaultly.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class LedgerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transaction")
    List<LedgerEntry> ledgerEntries;

    @Enumerated(EnumType.STRING)
    TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Fund fund;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    String description;
    UUID idempotencyKey;
}
