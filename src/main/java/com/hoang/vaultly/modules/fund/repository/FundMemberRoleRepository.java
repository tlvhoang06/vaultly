package com.hoang.vaultly.modules.fund.repository;

import com.hoang.vaultly.modules.fund.entity.FundMemberRole;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FundMemberRoleRepository extends JpaRepository<FundMemberRole, UUID> {
    // avoid assigning the same role in a fund
    boolean existsByFundMember_IdAndRole(UUID fundMemberId, FundRole role);

}
