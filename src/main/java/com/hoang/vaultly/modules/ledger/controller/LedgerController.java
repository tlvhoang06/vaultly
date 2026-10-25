package com.hoang.vaultly.modules.ledger.controller;

import com.hoang.vaultly.modules.ledger.dto.request.LedgerTransactionRequest;
import com.hoang.vaultly.modules.ledger.dto.response.LedgerTransactionResponse;
import com.hoang.vaultly.modules.ledger.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/funds/{fundId}/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    @PostMapping("/deposit")
    public LedgerTransactionResponse deposit(@PathVariable UUID fundId, @Valid LedgerTransactionRequest request){
        return ledgerService.deposit(fundId, request);
    }
}
