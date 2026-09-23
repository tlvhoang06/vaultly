package com.hoang.vaultly.modules.fund.controller;

import com.hoang.vaultly.modules.fund.dto.request.FundCreationRequest;
import com.hoang.vaultly.modules.fund.dto.response.FundCreationResponse;
import com.hoang.vaultly.modules.fund.service.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fund")
@RequiredArgsConstructor
public class FundController {
    private final FundService fundService;

    @PostMapping("/create-fund")
    public FundCreationResponse createFund(@Valid @RequestBody FundCreationRequest request){
        return fundService.createFund(request);
    }
}
