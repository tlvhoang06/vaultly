package com.hoang.vaultly.modules.fund.security;

import com.hoang.vaultly.modules.fund.enums.FundPermission;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.fund.repository.FundMemberRepository;
import com.hoang.vaultly.modules.fund.repository.FundMemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service("fundSecurity")
@Slf4j
@RequiredArgsConstructor
public class FundSecurityService {
    private final FundMemberRoleRepository fundMemberRoleRepository;
    private final FundMemberRepository fundMemberRepository;

    // Use for Annotations that received text-based permission and convert to enum
    @Transactional(readOnly = true)
    public boolean hasPermission(Authentication authentication, UUID fundId, String permissionName){
        if(permissionName == null){
            return false;
        }
        try{
            return hasPermission(authentication, fundId, FundPermission.valueOf(permissionName));
        }
        catch (IllegalArgumentException e){
            log.error("Invalid FundPermission: {}", permissionName);
            return false;
        }
    }

    // Use for internal direct service call
    @Transactional(readOnly = true)
    public boolean hasPermission(UUID fundId, FundPermission permission){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasPermission(authentication, fundId, permission);
    }

    // Main function
    @Transactional(readOnly = true)
    public boolean hasPermission(Authentication authentication, UUID fundId, FundPermission permission){
        String username = extractUsername(authentication);
        if(username == null || fundId == null || permission == null){
            return false;
        }

        Set<FundRole> roles = fundMemberRoleRepository.findRolesByFundIdAndUsername(fundId, username);

        return roles.stream().anyMatch(role -> role.hasPermission(permission));
    }

    @Transactional(readOnly = true)
    public boolean canViewMemberBalance(Authentication authentication, UUID fundId, UUID memberId){
        String username = extractUsername(authentication);
        if(username == null || fundId == null || memberId == null){
            return false;
        }

        Set<FundRole> roles = fundMemberRoleRepository.findRolesByFundIdAndUsername(fundId, username);

        // If has READ_ALL permission (OWNER, MANAGER, AUDITOR) -> allow immediately
        if(roles.stream().anyMatch(role -> role.hasPermission(FundPermission.LEDGER_READ_ALL))){
            return true;
        }

        // If only has READ_OWN permission (INVESTOR) -> memberId has to be the same as service caller's
        if(roles.stream().anyMatch(role -> role.hasPermission(FundPermission.LEDGER_READ_OWN))){
            return fundMemberRepository.existsByIdAndFund_IdAndUser_Username(memberId, fundId, username);
    }

        return false;

    }

    private String extractUsername(Authentication authentication){
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken)
            return null;
        return authentication.getName();
    }
}
