package com.hoang.vaultly.modules.fund.repository;

import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FundRepository extends JpaRepository<Fund, UUID> {
    // avoid assigning two fund owners
    boolean existsByRole(FundRole role);
}
