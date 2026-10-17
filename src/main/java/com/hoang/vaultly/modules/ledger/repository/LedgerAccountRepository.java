package com.hoang.vaultly.modules.ledger.repository;

import com.hoang.vaultly.modules.ledger.entity.LedgerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {
}
