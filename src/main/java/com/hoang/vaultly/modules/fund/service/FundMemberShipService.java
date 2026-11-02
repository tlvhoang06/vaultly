package com.hoang.vaultly.modules.fund.service;

import com.hoang.vaultly.common.exception.AppException;
import com.hoang.vaultly.common.exception.ErrorCode;
import com.hoang.vaultly.modules.fund.dto.request.InviteMemberRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundMemberResponse;
import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.fund.entity.FundMemberRole;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.fund.mapper.FundMemberMapper;
import com.hoang.vaultly.modules.fund.repository.FundMemberRepository;
import com.hoang.vaultly.modules.fund.repository.FundRepository;
import com.hoang.vaultly.modules.ledger.service.LedgerAccountService;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundMemberShipService {
    private final FundMemberMapper fundMemberMapper;
    private final UserRepository userRepository;
    private final FundRepository fundRepository;
    private final FundMemberRepository fundMemberRepository;
    private final LedgerAccountService ledgerAccountService;

    public List<FundMemberResponse> getMembers(UUID fundId) {
        if (!fundRepository.existsById(fundId)) {
            throw new AppException(ErrorCode.FUND_NOT_FOUND);
        }
        return fundMemberRepository.findByFund_Id(fundId)
                .stream()
                .map(fundMemberMapper::toFundMemberResponse)
                .toList();
    }

    public FundMemberResponse inviteMember(UUID fundId, InviteMemberRequest request) {
        if (request.roles() != null && request.roles().contains(FundRole.FUND_OWNER)) {
            throw new AppException(ErrorCode.CANNOT_ASSIGN_OWNER);
        }
        if (fundMemberRepository.existsByFund_IdAndUser_Id(fundId, request.userId())) {
            throw new AppException(ErrorCode.ALREADY_FUND_MEMBER);
        }
        User user = userRepository
                .findById(request.userId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new AppException(ErrorCode.FUND_NOT_FOUND));
        FundMember fundMember = FundMember.builder().user(user).fund(fund).build();

        if (request.roles() != null) {
            request.roles().forEach(fundMember::addRole);
        }

        FundMember savedMember = fundMemberRepository.save(fundMember);
        ledgerAccountService.openMemberAccounts(savedMember);

        return fundMemberMapper.toFundMemberResponse(savedMember);
    }

    public FundMemberResponse assignRole(UUID fundId, UUID fundMemberId, FundRole role) {
        if (role == FundRole.FUND_OWNER) {
            throw new AppException(ErrorCode.CANNOT_ASSIGN_OWNER);
        }

        FundMember fundMember = fundMemberRepository
                .findById(fundMemberId)
                .orElseThrow(() -> new AppException(ErrorCode.FUND_MEMBER_NOT_FOUND));

        if (!fundMember.getFund().getId().equals(fundId)) {
            throw new AppException(ErrorCode.MEMBER_NOT_IN_FUND);
        }

        boolean roleExists = fundMember
                .getRoles()
                .stream()
                .map(FundMemberRole::getRole)
                .collect(Collectors.toSet())
                .contains(role);
        if (roleExists) {
            throw new AppException(ErrorCode.MEMBER_ROLE_EXISTS);
        }

        fundMember.addRole(role);
        FundMember savedMember = fundMemberRepository.save(fundMember);

        return fundMemberMapper.toFundMemberResponse(savedMember);
    }

    public FundMemberResponse removeRole(UUID fundId, UUID memberId, FundRole role) {
        if (role.equals(FundRole.FUND_OWNER)) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_OWNER);
        }

        FundMember fundMember = fundMemberRepository
                .findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.FUND_MEMBER_NOT_FOUND));

        if (!fundMember.getFund().getId().equals(fundId)) {
            throw new AppException(ErrorCode.MEMBER_NOT_IN_FUND);
        }

        if (fundMember.getRoles().size() <= 1) {
            throw new AppException(ErrorCode.MEMBER_MUST_HAVE_ONE_ROLE);
        }

        Set<FundRole> roleSet = fundMemberMapper.extractRoles(fundMember);
        if (!roleSet.contains(role)) {
            throw new AppException(ErrorCode.MEMBER_ROLE_NOT_FOUND);
        }

        fundMember.getRoles().removeIf(r -> r.getRole() == role);
        FundMember savedMember = fundMemberRepository.save(fundMember);

        return fundMemberMapper.toFundMemberResponse(savedMember);
    }

    public void removeMember(UUID fundId, UUID memberId) {
        FundMember member = fundMemberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.FUND_MEMBER_NOT_FOUND));

        if (!member.getFund().getId().equals(fundId)) {
            throw new AppException(ErrorCode.MEMBER_NOT_IN_FUND);
        }

        boolean isOwner = member.getRoles().stream()
                .anyMatch(r -> r.getRole() == FundRole.FUND_OWNER);
        if (isOwner) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_OWNER);
        }

        ledgerAccountService.prepareMemberRemoval(memberId);
        fundMemberRepository.delete(member);
    }
}