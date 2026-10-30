package com.hoang.vaultly.modules.ledger.repository;


import com.hoang.vaultly.modules.ledger.entity.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, UUID> {
    boolean existsByFund_IdAndIdempotencyKey(UUID fundId, UUID idempotencyKey);
    List<LedgerTransaction> findByFund_IdOrderByCreatedAtDesc(UUID fundId);
}
