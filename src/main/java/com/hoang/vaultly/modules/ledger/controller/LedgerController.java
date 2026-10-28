package com.hoang.vaultly.modules.ledger.controller;

import com.hoang.vaultly.modules.ledger.dto.request.LedgerTransactionRequest;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/funds/{fundId}/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    @PostMapping("/deposit")
    public LedgerTransactionResponse deposit(
            @PathVariable UUID fundId,
            @Valid @RequestBody LedgerTransactionRequest request
    ) {
        return ledgerService.deposit(fundId, request);
    }

    @PostMapping("/withdrawal")
    public LedgerTransactionResponse withdrawal(
            @PathVariable UUID fundId,
            @Valid @RequestBody LedgerTransactionRequest request
    ) {
        return ledgerService.withdrawal(fundId, request);
    }
}
