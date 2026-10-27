package com.hoang.vaultly.modules.ledger.repository;

import com.hoang.vaultly.modules.ledger.entity.LedgerAccount;
import com.hoang.vaultly.modules.ledger.enums.AccountType;
import com.hoang.vaultly.modules.ledger.enums.EntryType;
import com.hoang.vaultly.modules.ledger.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {
    LedgerAccount findByFund_IdAndAccountType(UUID fundId, AccountType accountType);

    LedgerAccount findByFundMember_IdAndAccountType(UUID fundMemberId, AccountType accountType);

    @Query("""
    SELECT COALESCE(
        SUM(CASE WHEN e.entryType = :normalSide THEN e.amount ELSE -e.amount END),
        0
    )
    FROM LedgerEntry e
    WHERE e.ledgerAccount.id = :accountId
      AND e.transaction.transactionStatus = :status
""")
    BigDecimal calculateAccountBalance(
            @Param("accountId") UUID accountId,
            @Param("normalSide") EntryType normalSide,
            @Param("status") TransactionStatus status
    );

    default BigDecimal calculateAccountBalance(UUID accountId, EntryType normalSide) {
        return calculateAccountBalance(accountId, normalSide, TransactionStatus.COMPLETED);
    }
}
