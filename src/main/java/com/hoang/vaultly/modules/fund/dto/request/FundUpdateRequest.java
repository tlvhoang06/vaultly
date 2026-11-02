package com.hoang.vaultly.modules.fund.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FundUpdateRequest(
        @NotBlank(message = "Fund name cannot be blank")
        @Size(max = 100, message = "Fund name cannot exceed 100 characters")
        String name,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description
) {}
