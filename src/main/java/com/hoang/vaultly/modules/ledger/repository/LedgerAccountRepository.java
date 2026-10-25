package com.hoang.vaultly.modules.ledger.repository;

import com.hoang.vaultly.modules.ledger.entity.LedgerAccount;
import com.hoang.vaultly.modules.ledger.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LedgerAccountRepository extends JpaRepository<LedgerAccount, UUID> {
    LedgerAccount findByFund_IdAndAccountType(UUID fundId, AccountType accountType);
    LedgerAccount findByFundMember_IdAndAccountType(UUID fundMemberId, AccountType accountType);
}
