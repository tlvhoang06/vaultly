package com.hoang.vaultly.modules.ledger.controller;

import com.hoang.vaultly.common.dto.ApiResponse;
import com.hoang.vaultly.modules.ledger.dto.request.LedgerTransactionRequest;
import com.hoang.vaultly.modules.ledger.dto.response.FundBalanceResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionDetailResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.dto.response.MemberBalanceResponse;
import com.hoang.vaultly.modules.ledger.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/funds/{fundId}/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    @PostMapping("/deposit")
    public ApiResponse<LedgerTransactionResponse> deposit(
            @PathVariable UUID fundId,
            @Valid @RequestBody LedgerTransactionRequest request
    ) {
        return ApiResponse.success(ledgerService.deposit(fundId, request));
    }

    @PostMapping("/withdrawal")
    public ApiResponse<LedgerTransactionResponse> withdrawal(
            @PathVariable UUID fundId,
            @Valid @RequestBody LedgerTransactionRequest request
    ) {
        return ApiResponse.success(ledgerService.withdrawal(fundId, request));
    }

    @PostMapping("/expense")
    public ApiResponse<LedgerTransactionResponse> expense(
            @PathVariable UUID fundId,
            @Valid @RequestBody LedgerTransactionRequest request
    ) {
        return ApiResponse.success(ledgerService.expense(fundId, request));
    }

    @GetMapping("/balance")
    public ApiResponse<FundBalanceResponse> getFundBalance(@PathVariable UUID fundId){
        return ApiResponse.success(ledgerService.getFundBalance(fundId));
    }

    @GetMapping("/members/{memberId}/balance")
    public ApiResponse<MemberBalanceResponse> getMemberBalance(
            @PathVariable UUID fundId,
            @PathVariable UUID memberId
    ) {
        return ApiResponse.success(ledgerService.getMemberBalance(fundId, memberId));
    }

    @GetMapping("/transactions")
    public ApiResponse<Page<LedgerTransactionResponse>> getTransactions(
            @PathVariable UUID fundId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ApiResponse.success(ledgerService.getTransactions(fundId, pageable));
    }

    @GetMapping("/transactions/{transactionId}")
    public ApiResponse<LedgerTransactionDetailResponse> getTransactionDetail(
            @PathVariable UUID fundId,
            @PathVariable UUID transactionId
    ){
        return ApiResponse.success(ledgerService.getTransactionDetail(fundId, transactionId));
    }
}