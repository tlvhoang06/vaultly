package com.hoang.vaultly.modules.fund.repository;

import com.hoang.vaultly.modules.fund.entity.FundMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FundMemberRepository extends JpaRepository<FundMember, UUID> {
    Optional<FundMember> findByFund_IdAndUser_Id(UUID fundId, UUID userId);
    boolean existsByFund_IdAndUser_Id(UUID fundId, UUID userId);
    List<FundMember> findByUser_Id(UUID userId);
    List<FundMember> findByFund_Id(UUID fundId);
}
