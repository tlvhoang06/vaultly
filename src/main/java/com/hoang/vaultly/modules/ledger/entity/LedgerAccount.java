package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.ledger.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "ledger_accounts",
        check = @CheckConstraint(
                name = "ck_ledger_account_member_scope",
                constraint = "(account_type = 'MEMBER_EQUITY') = (fund_member_id IS NOT NULL)"
        )
)
@NoArgsConstructor
public class LedgerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false)
    Fund fund;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "fund_member_id", nullable = true)
    FundMember fundMember;

    @Enumerated(EnumType.STRING)
    AccountType accountType;

    public static LedgerAccount ofFund(Fund fund, AccountType type){
        if(type.getScope() != AccountType.Scope.FUND){
            throw new IllegalArgumentException(type + " is not a fund-level account");
        }
        LedgerAccount account = new LedgerAccount();
        account.fund = fund;
        account.accountType = type;
        return account;
    }

    public static LedgerAccount ofMember(FundMember member, AccountType type){
        if(type.getScope() != AccountType.Scope.MEMBER){
            throw new IllegalArgumentException(type + " is not a member-level account");
        }
        LedgerAccount account = new LedgerAccount();
        account.fund = member.getFund();
        account.fundMember = member;
        account.accountType = type;
        return account;
    }
}
