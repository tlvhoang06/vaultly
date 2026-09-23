package com.hoang.vaultly.modules.fund.repository;

import com.hoang.vaultly.modules.fund.entity.FundMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FundMemberRepository extends JpaRepository<FundMember, UUID> {
}
