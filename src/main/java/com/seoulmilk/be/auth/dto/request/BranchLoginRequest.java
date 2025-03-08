package com.seoulmilk.be.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record BranchLoginRequest(
        @Schema(description = "사업자 등록 번호")
        @NotBlank
        String businessId,

        @NotBlank
        String password
) {
}
