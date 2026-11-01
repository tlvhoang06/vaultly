package com.hoang.vaultly.modules.ledger.repository;

import com.hoang.vaultly.modules.ledger.entity.LedgerTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, UUID> {
    boolean existsByFund_IdAndIdempotencyKey(UUID fundId, UUID idempotencyKey);
    List<LedgerTransaction> findByFund_IdOrderByCreatedAtDesc(UUID fundId);
    Page<LedgerTransaction> findByFund_Id(UUID fundId, Pageable pageable);
    Optional<LedgerTransaction> findByIdAndFund_Id(UUID transactionId, UUID fundId);
}
