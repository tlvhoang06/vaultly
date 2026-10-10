package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.ledger.enums.EntryType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class LedgerEntry {
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    LedgerTransaction transaction;

    EntryType entryType;
}
