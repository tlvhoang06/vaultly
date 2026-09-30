package com.hoang.vaultly.modules.fund.controller;

import com.hoang.vaultly.modules.fund.dto.request.AssignRoleRequest;
import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.request.InviteMemberRequest;
import com.hoang.vaultly.modules.fund.dto.request.RemoveRoleRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.dto.response.FundMemberResponse;
import com.hoang.vaultly.modules.fund.service.FundMemberShipService;
import com.hoang.vaultly.modules.fund.service.FundService;
import com.hoang.vaultly.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;
    private final FundMemberShipService fundMemberShipService;
    private final UserRepository userRepository;

    @PostMapping("/create-fund")
    public FundCreationResponse createFund(@Valid @RequestBody FundCreationRequest request) {
        return fundService.createFund(request);
    }

    @PostMapping("/{fundId}/members")
    public FundMemberResponse inviteMember(@PathVariable UUID fundId,
                                           @Valid @RequestBody InviteMemberRequest request) {
        return fundMemberShipService.inviteMember(fundId, request);
    }

    @PostMapping("/{fundId}/members/{memberId}/roles")
    public FundMemberResponse assignRole(@PathVariable UUID fundId,
                                         @PathVariable UUID memberId,
                                         @Valid @RequestBody AssignRoleRequest request) {
        return fundMemberShipService.assignRole(fundId, memberId, request.role());
    }

    @DeleteMapping("/{fundId}/members/{memberId}/roles")
    public FundMemberResponse removeRole(@PathVariable UUID fundId,
                                        @PathVariable UUID memberId,
                                        @Valid @RequestBody RemoveRoleRequest request) {
        return fundMemberShipService.removeRole(fundId, memberId, request.role());
    }

}
