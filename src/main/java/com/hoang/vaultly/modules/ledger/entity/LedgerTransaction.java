package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.ledger.enums.EntryType;
import com.hoang.vaultly.modules.ledger.enums.TransactionStatus;
import com.hoang.vaultly.modules.ledger.enums.TransactionType;
import com.hoang.vaultly.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor
@Table(name = "ledger_transaction", uniqueConstraints = @UniqueConstraint(columnNames = {"fund_id",
        "idempotency_key"}))
public class LedgerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transaction", cascade = CascadeType.PERSIST)
    List<LedgerEntry> ledgerEntries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false, updatable = false)
    Fund fund;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    String description;

    @Column(name = "idempotency_key", nullable = false, updatable = false)
    UUID idempotencyKey;

    public void recordEntry(LedgerAccount account, EntryType entryType, BigDecimal amount) {
        LedgerEntry entry = LedgerEntry.builder()
                .entryType(entryType)
                .ledgerAccount(account)
                .amount(amount)
                .transaction(this)
                .build();
        this.ledgerEntries.add(entry);
    }
}
