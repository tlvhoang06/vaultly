package com.hoang.vaultly.modules.ledger.controller;

import com.hoang.vaultly.common.dto.ApiResponse;
import com.hoang.vaultly.modules.ledger.dto.request.LedgerTransactionRequest;
import com.hoang.vaultly.modules.ledger.dto.response.FundBalanceResponse;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/transactions")
    public ApiResponse<List<LedgerTransactionResponse>> getTransactions(@PathVariable UUID fundId){
        return ApiResponse.success(ledgerService.getTransactions(fundId));
    }
}