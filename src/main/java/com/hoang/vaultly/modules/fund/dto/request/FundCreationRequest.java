package com.hoang.vaultly.modules.fund.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FundCreationRequest(
        String name,
        String description,

        @NotBlank
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
        String currency
) {
}
