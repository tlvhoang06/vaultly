package com.hoang.vaultly.modules.fund.controller;

import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.service.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fund/v1")
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;
    public FundCreationResponse createFund(@Valid FundCreationRequest request){
        return fundService.createFund(request);
    }
}
