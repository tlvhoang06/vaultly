package com.hoang.vaultly.modules.ledger.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.ledger.entity.LedgerAccount;
import com.hoang.vaultly.modules.ledger.enums.AccountType;
import com.hoang.vaultly.modules.ledger.repository.LedgerAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LedgerAccountService {
    private final LedgerAccountRepository ledgerAccountRepository;
    /**
     LOGIC:
     - Fund: When creating a Fund, the system provides 4 ledger accounts:
     CASH, INVESTMENT, EXPENSE, REVENUE for that fund
     - Member: When a member join a fund, the system provides a ledger account MEMBER_EQUITY
     for that member (scalable)

     */


    // open all account types in scope Fund
    public void openFundAccounts(Fund fund) {
        AccountType.byScope(AccountType.Scope.FUND)
                .forEach(t -> ledgerAccountRepository
                        .save(LedgerAccount.ofFund(fund, t)));

    }

    // open all account types in scope FundMember
    public void openMemberAccounts(FundMember fundMember) {
        AccountType.byScope(AccountType.Scope.MEMBER)
                .forEach(t -> ledgerAccountRepository
                        .save(LedgerAccount.ofMember(fundMember, t)));
    }

    public void prepareMemberRemoval(UUID fundMemberId) {
        Optional<LedgerAccount> accountOpt = ledgerAccountRepository
                .findByFundMember_IdAndAccountType(fundMemberId, AccountType.MEMBER_EQUITY);
        if (accountOpt.isPresent()) {
            LedgerAccount account = accountOpt.get();
            BigDecimal balance = ledgerAccountRepository.calculateAccountBalance(
                    account.getId(), account.getAccountType().getNormalSide());
            if (balance.compareTo(BigDecimal.ZERO) != 0) {
                throw new AppException(ErrorCode.MEMBER_HAS_ACTIVE_BALANCE);
            }
            if (ledgerAccountRepository.hasEntries(account.getId())) {
                throw new AppException(ErrorCode.CANNOT_REMOVE_MEMBER_WITH_TRANSACTIONS);
            }
            ledgerAccountRepository.delete(account);
        }
    }
}
