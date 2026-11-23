package com.hoang.vaultly.modules.fund.security;

import com.hoang.vaultly.modules.fund.enums.FundPermission;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
        "@fundSecurity.hasPermission(authentication, #fundId, '{value}')"
)
public @interface RequireFundPermission {
    FundPermission value();
}
