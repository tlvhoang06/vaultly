package com.hoang.vaultly.modules.fund.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.dto.response.FundResponse;
import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.fund.enums.FundStatus;
import com.hoang.vaultly.modules.fund.mapper.FundMapper;
import com.hoang.vaultly.modules.fund.repository.FundMemberRepository;
import com.hoang.vaultly.modules.fund.repository.FundRepository;
import com.hoang.vaultly.modules.ledger.service.LedgerAccountService;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FundService {
    private final FundRepository fundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final UserService userService;
    private final LedgerAccountService ledgerAccountService;
    private final FundMapper fundMapper;

    @Transactional
    public FundCreationResponse createFund(FundCreationRequest request) {
        User currentUser = userService.getCurrentUser();

        Fund fund = fundMapper.toFund(request);
        fund.setStatus(FundStatus.ACTIVE);
        fund.setCreatedBy(currentUser);

        // bidirectional mapping
        var owner = FundMember.builder()
                .user(currentUser)
                .build();
        owner.addRole(FundRole.FUND_OWNER);
        fund.addMember(owner);

        var savedFund = fundRepository.save(fund);

        ledgerAccountService.openFundAccounts(fund);
        ledgerAccountService.openMemberAccounts(owner);

        return fundMapper.toFundCreationResponse(savedFund);
    }

    public List<FundResponse> getMyFunds() {
        User currentUser = userService.getCurrentUser();
        return fundMemberRepository.findByUser_Id(currentUser.getId())
                .stream()
                .map(fundMapper::toFundResponse)
                .toList();
    }

    public FundResponse getFundById(UUID fundId) {
        User currentUser = userService.getCurrentUser();
        FundMember member = fundMemberRepository.findByFund_IdAndUser_Id(fundId, currentUser.getId())
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_IN_FUND));

        return fundMapper.toFundResponse(member);
    }
}
