package com.hoang.vaultly.modules.user.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum SystemRole {
    USER(Set.of()),
    ADMIN(Set.of(
            SystemPermission.USER_DELETE,
            SystemPermission.USER_LOCK,
            SystemPermission.USER_VIEW_ALL
    ));

    private final Set<SystemPermission> permissions;

    SystemRole(Set<SystemPermission> permissions) {
        this.permissions = permissions;
    }

}


