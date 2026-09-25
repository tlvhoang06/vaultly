package com.hoang.vaultly.modules.fund.service;

import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.entity.Fund;
import com.hoang.vaultly.modules.fund.entity.FundMember;
import com.hoang.vaultly.modules.fund.enums.FundRole;
import com.hoang.vaultly.modules.fund.enums.FundStatus;
import com.hoang.vaultly.modules.fund.mapper.FundMapper;
import com.hoang.vaultly.modules.fund.repository.FundRepository;
import com.hoang.vaultly.modules.user.entity.User;
import com.hoang.vaultly.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FundService {
    private final FundRepository fundRepository;
    private final UserService userService;
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

        // [x]
        //fundRepository.save(fund);
        //return fundMapper.toFundCreationResponse(fund);

        // [v]
        // Hibernate assign @GeneratedValue and @CreationTimestamp after persist/flush
        // -> Spring Data JPA: Should store objects returned by repo.save()
        // to ensure that instance has both ID and Timestamp before go through mapper
        var savedFund = fundRepository.save(fund);
        return fundMapper.toFundCreationResponse(savedFund);
    }
}
