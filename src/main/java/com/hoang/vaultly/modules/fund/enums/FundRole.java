package com.hoang.vaultly.modules.fund.enums;
import java.util.Set;

public enum FundRole {
    FUND_OWNER(Set.of(
            FundPermission.FUND_READ,
            FundPermission.FUND_CLOSE,
            FundPermission.FUND_UPDATE,

            FundPermission.MEMBER_INVITE,
            FundPermission.MEMBER_REMOVE,
            FundPermission.ROLE_MANAGE,

            FundPermission.LEDGER_READ_OWN,
            FundPermission.LEDGER_READ_ALL,
            FundPermission.LEDGER_DEPOSIT,
            FundPermission.LEDGER_WITHDRAW,
            FundPermission.LEDGER_EXPENSE
    )),
    FUND_MANAGER(Set.of(
            FundPermission.FUND_READ,

            FundPermission.LEDGER_READ_OWN,
            FundPermission.LEDGER_READ_ALL,
            FundPermission.LEDGER_DEPOSIT,
            FundPermission.LEDGER_WITHDRAW,
            FundPermission.LEDGER_EXPENSE
    )),
    FUND_INVESTOR(Set.of(
            FundPermission.FUND_READ,

            FundPermission.LEDGER_READ_OWN,
            FundPermission.LEDGER_DEPOSIT,
            FundPermission.LEDGER_WITHDRAW
    )),
    FUND_AUDITOR(Set.of(
            FundPermission.FUND_READ,
            FundPermission.LEDGER_READ_OWN,
            FundPermission.LEDGER_READ_ALL
    ));

    private final Set<FundPermission> permissions;

    FundRole(Set<FundPermission> permissions){
        this.permissions = permissions;
    }

    public boolean hasPermission(FundPermission permission){
        return permission != null && this.permissions.contains(permission);
    }
}