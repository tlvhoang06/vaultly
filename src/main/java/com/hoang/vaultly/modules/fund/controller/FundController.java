package com.hoang.vaultly.modules.fund.controller;

import com.hoang.vaultly.common.dto.ApiResponse;
import com.hoang.vaultly.modules.fund.dto.request.AssignRoleRequest;
import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.request.InviteMemberRequest;
import com.hoang.vaultly.modules.fund.dto.request.RemoveRoleRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.dto.response.FundMemberResponse;
import com.hoang.vaultly.modules.fund.dto.response.FundResponse;
import com.hoang.vaultly.modules.fund.service.FundMemberShipService;
import com.hoang.vaultly.modules.fund.service.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;
    private final FundMemberShipService fundMemberShipService;

    @PostMapping
    public ApiResponse<FundCreationResponse> createFund(@Valid @RequestBody FundCreationRequest request) {
        return ApiResponse.success(fundService.createFund(request));
    }

    @GetMapping
    public ApiResponse<List<FundResponse>> getMyFunds() {
        return ApiResponse.success(fundService.getMyFunds());
    }

    @GetMapping("/{fundId}")
    public ApiResponse<FundResponse> getFundById(@PathVariable UUID fundId) {
        return ApiResponse.success(fundService.getFundById(fundId));
    }

    @GetMapping("/{fundId}/members")
    public ApiResponse<List<FundMemberResponse>> getMembers(@PathVariable UUID fundId) {
        return ApiResponse.success(fundMemberShipService.getMembers(fundId));
    }

    @PostMapping("/{fundId}/members")
    public ApiResponse<FundMemberResponse> inviteMember(@PathVariable UUID fundId,
                                                       @Valid @RequestBody InviteMemberRequest request) {
        return ApiResponse.success(fundMemberShipService.inviteMember(fundId, request));
    }

    @PostMapping("/{fundId}/members/{memberId}/roles")
    public ApiResponse<FundMemberResponse> assignRole(@PathVariable UUID fundId,
                                                     @PathVariable UUID memberId,
                                                     @Valid @RequestBody AssignRoleRequest request) {
        return ApiResponse.success(fundMemberShipService.assignRole(fundId, memberId, request.role()));
    }

    @DeleteMapping("/{fundId}/members/{memberId}/roles")
    public ApiResponse<FundMemberResponse> removeRole(@PathVariable UUID fundId,
                                                     @PathVariable UUID memberId,
                                                     @Valid @RequestBody RemoveRoleRequest request) {
        return ApiResponse.success(fundMemberShipService.removeRole(fundId, memberId, request.role()));
    }

    @DeleteMapping("/{fundId}/members/{memberId}")
    public ApiResponse<String> removeMember(@PathVariable UUID fundId,
                                           @PathVariable UUID memberId) {
        fundMemberShipService.removeMember(fundId, memberId);
        return ApiResponse.success("Member removed successfully", null);
    }
}
