package com.hoang.vaultly.modules.ledger.entity;

import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.ledger.enums.AccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class LedgerAccount {
    @ManyToOne
    @JoinColumn(name = "fund_id")
    Fund fund;

    @ManyToOne
    @JoinColumn(name = "fund_member_id")
    FundMember fundMember;

    AccountType accountType;

}
