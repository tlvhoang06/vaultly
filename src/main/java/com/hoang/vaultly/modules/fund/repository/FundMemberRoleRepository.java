package com.hoang.vaultly.modules.fund.repository;

import com.hoang.vaultly.modules.fund.entity.FundMemberRole;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface FundMemberRoleRepository extends JpaRepository<FundMemberRole, UUID> {
    // avoid assigning the same role in a fund
    boolean existsByFundMember_IdAndRole(UUID fundMemberId, FundRole role);

    @Query("""
            SELECT r.role
            FROM FundMemberRole r
            WHERE r.fundMember.fund.id = :fundId
            AND r.fundMember.user.username = :username
            """)
    Set<FundRole> findRolesByFundIdAndUsername(@Param("fundId") UUID fundId, @Param("username") String username);
}
