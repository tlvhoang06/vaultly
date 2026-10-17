package com.hoang.vaultly.modules.ledger.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Getter
public enum AccountType {
    CASH(Scope.FUND, EntryType.DEBIT),
    INVESTMENT(Scope.FUND, EntryType.DEBIT),
    EXPENSE(Scope.FUND, EntryType.DEBIT),
    REVENUE(Scope.FUND, EntryType.CREDIT),
    MEMBER_EQUITY(Scope.MEMBER, EntryType.CREDIT);

    public enum Scope {
        FUND,
        MEMBER
    }

    private final Scope scope;
    private final EntryType normalSide;

    AccountType(Scope scope, EntryType normalSide) {
        this.scope = scope;
        this.normalSide = normalSide;
    }

    public BigDecimal signedAmount(EntryType side, BigDecimal amount) {
        return side == normalSide ? amount : amount.negate();
    }

    public static List<AccountType> byScope(Scope scope) {
        return Arrays.stream(values()).filter(t -> t.scope == scope).toList();
    }
}
